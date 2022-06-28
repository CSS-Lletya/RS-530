package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 120, description = "Represents an event where a Player adds another Player to their friends list")
public class AddFriendPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		if (name > 0) {
			player.getFriends().addFriend(name);
		}
	}
}