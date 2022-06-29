package com.xeno.packetbuilder.packets.impl;

import com.xeno.content.Trade;
import com.xeno.content.TradeSession;
import com.xeno.entity.actor.masks.FaceLocation;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.AreaTask;
import com.xeno.net.Constants;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.world.World;

@OutgoingPacketSignature(packetId = 71, description = "Represents an event where a Player accepts a trade request with another Player")
public class AcceptTradeRequestPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
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
			World.getInstance().registerCoordinateEvent(new AreaTask(player, x-1, y-1, x+1, y+1) {
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