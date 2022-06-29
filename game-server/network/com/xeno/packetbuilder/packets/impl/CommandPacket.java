package com.xeno.packetbuilder.packets.impl;

import com.rs.plugin.standard.CommandPluginDispatcher;
import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 44, description = "A command that the Player is sending to the client")
public class CommandPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		String command = packet.readRS2String();
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return;
			switch (cmd[0].toLowerCase()) {
			case "tele":
				System.out.println("?");
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.teleport(new Location(x,y,plane));//need to update tile apparently, not sure why its nto moved but npcs are moved. weird.
				return;
		}
		CommandPluginDispatcher.execute(player, cmd, command);
	}
}