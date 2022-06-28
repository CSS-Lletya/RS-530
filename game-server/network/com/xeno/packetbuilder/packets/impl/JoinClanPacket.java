package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.util.Utility;
import com.xeno.world.World;

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