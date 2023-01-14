package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 57, description = "Represents an event where a Player removes another Player to their friends list")
public class RemoveFriendPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().removeFriend(name);
		}
	}
}