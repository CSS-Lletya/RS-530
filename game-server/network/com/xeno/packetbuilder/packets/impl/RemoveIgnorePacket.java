package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 213, description = "Represents an event where a Player removes another Player to their ignore list")
public class RemoveIgnorePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().removeIgnore(name);
		}
	}
}