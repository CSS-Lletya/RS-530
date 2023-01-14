package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;

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