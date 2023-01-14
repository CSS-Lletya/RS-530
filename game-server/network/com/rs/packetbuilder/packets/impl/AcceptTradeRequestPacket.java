package com.rs.packetbuilder.packets.impl;

import com.rs.content.Trade;
import com.rs.content.TradeSession;
import com.rs.entity.actor.attribute.Attribute;
import com.rs.entity.actor.player.Player;
import com.rs.entity.actor.player.task.AreaTask;
import com.rs.net.Constants;
import com.rs.net.Packet;
import com.rs.net.entity.masks.FaceLocation;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.world.World;

@OutgoingPacketSignature(packetId = 71, description = "Represents an event where a Player accepts a trade request with another Player")
public class AcceptTradeRequestPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		int index = packet.readLEShortA();
		if(index < 0 || index >= Constants.PLAYER_CAP || player.getAttributes().get(Attribute.DEAD).getBoolean() || player.getAttributes().get(Attribute.LOCKED).getBoolean()) {
			return;
		}
		final Player p2 = World.getInstance().getPlayerList().get(index);
		if (p2 == null) {
			return;
		}
		player.getInterfaceManager().closeInterfaces();
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
						player.getInterfaceManager().closeInterfaces();
						p2.getInterfaceManager().closeInterfaces();
						p2.setFaceLocation(new FaceLocation(player.getLocation()));
						player.setTrade(new TradeSession(new Trade(player, p2)));
						p2.setTrade(new TradeSession(new Trade(p2, player)));
						return;
					}
					player.setFaceLocation(new FaceLocation(p2.getLocation()));
					p2.getActionSender().sendMessage(player.getPlayerCredentials().getDisplayName() + ":tradereq:");
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
			player.getInterfaceManager().closeInterfaces();
			p2.getInterfaceManager().closeInterfaces();
			p2.setFaceLocation(new FaceLocation(player.getLocation()));
			player.setTrade(new TradeSession(new Trade(player, p2)));
			p2.setTrade(new TradeSession(new Trade(p2, player)));
			return;
		}
		player.setFaceLocation(new FaceLocation(p2.getLocation()));
		p2.getActionSender().sendMessage(player.getPlayerCredentials().getDisplayName() + ":tradereq:");
		player.getActionSender().sendMessage("Sending trade offer...");
		player.newTradeRequest(p2);
	}
}