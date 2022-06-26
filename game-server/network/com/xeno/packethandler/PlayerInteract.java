package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.combat.Combat;
import com.xeno.content.combat.MagicCombat;
import com.xeno.event.AreaEvent;
import com.xeno.model.World;
import com.xeno.model.masks.FaceLocation;
import com.xeno.model.player.Player;
import com.xeno.model.player.TradeSession;
import com.xeno.net.Constants;
import com.xeno.net.Packet;
import com.xeno.util.Area;
import com.xeno.world.Trade;

public class PlayerInteract implements PacketHandler {

	private static final int ATTACK = 68;
	private static final int FOLLOW = 71;
	private static final int TRADE = 180;
	private static final int MAGIC_ON_PLAYER = 195;
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case ATTACK:
				handleAttackPlayer(player, packet);
				break;
				
			case FOLLOW:
				handleFollowPlayer(player, packet);
				break;
				
			case TRADE:
				handleTradePlayer(player, packet);
				break;
				
			case MAGIC_ON_PLAYER:
				handleMagicOnPlayer(player, packet);
				break;
		}
	}

	private void handleAttackPlayer(final Player player, Packet packet) {
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
			return;
		}
		player.setFaceLocation(new FaceLocation(p2.getLocation()));
		player.getActionSender().closeInterfaces();
		Combat.newAttack(player, p2);
	}

	private void handleFollowPlayer(Player player, Packet packet) {
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
			return;
		}
		player.getFollow().setFollowing(p2);
	}

	private void handleTradePlayer(final Player player, Packet packet) {
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
			return;
		}
		player.setFaceLocation(new FaceLocation(p2.getLocation()));
		if(player.getTrade() != null) {
			player.getTrade().decline();
			return;
		}
		player.getActionSender().closeInterfaces();
		if (!player.getLocation().withinDistance(p2.getLocation(), 1)) {
			int x = p2.getLocation().getX();
			int y = p2.getLocation().getY();
			World.getInstance().registerCoordinateEvent(new AreaEvent(player, x-1, y-1, x+1, y+1) {
				@Override
				public void run() {
					player.getWalkingQueue().reset();
					player.getActionSender().clearMapFlag();
					if (p2.getGESession() != null || p2.getTrade() != null || p2.getShopSession() != null || p2.getBank().isBanking()) {
						player.getActionSender().sendMessage("That player is busy at the moment.");
						return;
					}
					if (p2.wantsToTrade(player)) {
						player.getActionSender().closeInterfaces();
						p2.getActionSender().closeInterfaces();
						p2.setFaceLocation(new FaceLocation(player.getLocation()));
						player.setTrade(new TradeSession(new Trade(player, p2)));
						p2.setTrade(new TradeSession(new Trade(p2, player)));
						return;
					}
					player.setFaceLocation(new FaceLocation(p2.getLocation()));
					p2.getActionSender().sendMessage(player.getPlayerDetails().getDisplayName() + ":tradereq:");
					player.getActionSender().sendMessage("Sending trade offer...");
					player.newTradeRequest(p2);
				}
			});
			return;
		}
		if (p2.getGESession() != null || p2.getTrade() != null || p2.getShopSession() != null || p2.getBank().isBanking()) {
			player.getActionSender().sendMessage("That player is busy at the moment.");
			return;
		}
		if (p2.wantsToTrade(player)) {
			player.getActionSender().closeInterfaces();
			p2.getActionSender().closeInterfaces();
			p2.setFaceLocation(new FaceLocation(player.getLocation()));
			player.setTrade(new TradeSession(new Trade(player, p2)));
			p2.setTrade(new TradeSession(new Trade(p2, player)));
			return;
		}
		player.newTradeRequest(p2);
		p2.getActionSender().sendMessage(player.getPlayerDetails().getDisplayName() + ":tradereq:");
		player.getActionSender().sendMessage("Sending trade offer...");
	}
	
	private void handleMagicOnPlayer(Player player, Packet packet) {
		int junk = packet.readShortA();
		int id = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		player.setTarget(p2);
		MagicCombat.newMagicAttack(player, p2, id,  interfaceId == 193);
	}

}