package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.plugin.standard.CommandPluginDispatcher;
import com.rs.world.Location;

@OutgoingPacketSignature(packetId = 44, description = "A command that the Player is sending to the client")
public class CommandPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		String command = packet.readRS2String();
		if (command == null)
			return;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return;
		switch (cmd[0].toLowerCase()) {
		case "tele":
			cmd = cmd[1].split(",");
			int plane = Integer.valueOf(cmd[0]);
			int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
			int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
			player.teleport(new Location(x, y, plane));// need to update tile apparently, not sure why its nto moved but
														// npcs are moved. weird.
			return;
		}
		CommandPluginDispatcher.execute(player, cmd, command);
	}
}