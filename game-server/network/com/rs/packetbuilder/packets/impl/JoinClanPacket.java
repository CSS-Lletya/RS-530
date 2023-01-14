package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.utility.Utility;
import com.rs.world.World;

@OutgoingPacketSignature(packetId = 104, description = "Represents an event where a Player attempts to join a clan chat")
public class JoinClanPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long clanOwner = packet.readLong();
		if (clanOwner < 0) {
			return;
		}
		if (clanOwner == 0) {
			World.getInstance().getClanManager().leaveChannel(player);
			return;
		}
		String ownerName = Utility.longToPlayerName(clanOwner).toLowerCase();
		World.getInstance().getClanManager().enterChannel(player, ownerName);
	}
}