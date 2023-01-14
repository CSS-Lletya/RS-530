package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 157, description = "Represents an event where a Player adjusting their privacy settings ingame")
public class PrivacySettingsPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		int publicStatus = packet.readByte();
		int privateStatus = packet.readByte();
		int tradeStatus = packet.readByte();
		player.getFriends().setPrivacyOption(publicStatus, privateStatus, tradeStatus);
	}
}