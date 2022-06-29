package com.xeno.packetbuilder.packets.impl;

import com.xeno.content.Clan;
import com.xeno.content.ClanUser;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.util.Utility;
import com.xeno.world.World;

@OutgoingPacketSignature(packetId = 188, description = "Represents an event where a Player adjusting a Players clan ranking")
public class ClanRankingPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		int rank = packet.readByteA();
		long name = packet.readLong();
		if (name < 0 || (rank < 0 || rank > 6)) {
			return;
		}
		Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
		if (clan != null) {
			ClanUser user = clan.getUserByName(Utility.longToPlayerName(name));
			if (user != null) {
				user.setClanRights(rank);
				World.getInstance().getClanManager().updateClan(clan);
			} 
			clan.getUsersWithRank().put(Utility.longToPlayerName(name), rank);
		}
	}
}