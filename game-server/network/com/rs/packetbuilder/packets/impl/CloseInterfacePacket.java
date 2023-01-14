package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 184, description = "A closing interface event")
public class CloseInterfacePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		if (player.getTrade() != null) {
			player.getTrade().decline();
		}
		player.getInterfaceManager().closeInterfaces();
		if(player.getPlayerCredentials().isResized()) {
			player.getActionSender().sendWindowPane(746);
		} else {
			player.getActionSender().sendWindowPane(548);
		}
	}
}