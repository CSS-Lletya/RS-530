package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = {245,21,93,22,75,243,98}, description = "Represents an event where a Player performs specific actions")
public class ClientActionsPacket implements OutgoingPacket {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, Packet packet) {
		if (packet.getId() == 243) {
			int windowType = packet.readByte() & 0xff;
			int windowWidth = packet.readShort();
			int windowHeight = packet.readShort();
			int junk = packet.readByte() & 0xff;
			player.getActionSender().configureGameScreen(windowType);
		}
	}
	/*
	 * Since we don't have a use this is just archival information for packet naming and its respective id.
	 * 
	<binds>245,21,93,22,75,243,98
    <int>245</int> <!-- Idle -->
    <int>21</int> <!-- Move camera -->
    <int>93</int> <!-- Ping -->
    <int>22</int> <!-- Window focus -->
    <int>75</int> <!-- Mouse -->
    <int>243</int> <!-- Window type -->
    <int>98</int> <!-- Toggle sound (sounds) -->
	</binds>
	 */
}