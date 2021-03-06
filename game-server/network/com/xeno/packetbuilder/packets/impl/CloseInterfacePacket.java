package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

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