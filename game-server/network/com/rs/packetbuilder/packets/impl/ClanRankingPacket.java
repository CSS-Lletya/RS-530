package com.rs.packetbuilder.packets.impl;

import com.rs.content.Clan;
import com.rs.content.ClanUser;
import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.utility.Utility;
import com.rs.world.World;

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