package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.TradeSession;
import com.xeno.entity.masks.FaceLocation;
import com.xeno.entity.player.Player;
import com.xeno.event.AreaEvent;
import com.xeno.net.Constants;
import com.xeno.net.Packet;
import com.xeno.world.Trade;
import com.xeno.world.World;

public class AcceptRequest implements PacketHandler {

	private static final int ACCEPT_TRADE = 71; // d
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case ACCEPT_TRADE:
				handleAcceptTrade(player, packet);
				break;
		}
	}

	private void handleAcceptTrade(final Player player, Packet packet) {
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
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
					if (p2.getTrade() != null || p2.getShopSession() != null || p2.getBank().isBanking()) {
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
		if (p2.getTrade() != null || p2.getShopSession() != null || p2.getBank().isBanking()) {
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

}
