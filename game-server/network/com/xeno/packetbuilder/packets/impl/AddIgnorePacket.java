package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 34, description = "Represents an event where a Player adds another Player to their ignore list")
public class AddIgnorePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().addIgnore(name);
		}
	}
}