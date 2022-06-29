package com.xeno.packetbuilder.packets.impl;

import com.xeno.content.Clan;
import com.xeno.content.ClanUser;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.world.World;

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