package com.rs.packetbuilder.packets.impl;

import com.rs.content.Clan;
import com.rs.content.ClanUser;
import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.world.World;

@OutgoingPacketSignature(packetId = 162, description = "Represents an event where a Player removes another Player to their friends list")
public class KickFromClanChatPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		if (name < 0) {
			return;
		}
		Clan clan = World.getInstance().getClanManager().getClanByPlayer(player, player.getUsername());
		if (clan != null) {
			ClanUser user = clan.getUserByName(player.getUsername());
			if (user != null) {
				if (user.getClanRights() < clan.getKickRights()) {
					player.getActionSender().sendMessage("You do not have a high enough rank to kick users from this clan chat.");
					return;
				}
				clan.kickUser(name);
			}
		}
	}
}