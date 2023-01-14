package com.rs.packetbuilder.packets.impl;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;
import com.rs.net.entity.masks.ChatMessage;
import com.rs.packetbuilder.packets.OutgoingPacket;
import com.rs.packetbuilder.packets.OutgoingPacketSignature;
import com.rs.utility.Utility;
import com.rs.world.World;

@OutgoingPacketSignature(packetId = 237, description = "Represents an event where a Player talks in the game server")
public class PublicChatPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		int colour = packet.readByte();
		int effects  = packet.readByte();
		//int size = packet.readByte();
		int size = packet.getLength() - 2;
		//String text = Misc.decryptPlayerChat(packet, size);
		byte[] chatData = new byte[size];
		chatData = packet.getRemainingData();
		String unpacked = Utility.textUnpack(chatData, size);
		byte[] packed = new byte[size];
		Utility.textPack(packed, unpacked);
		ChatMessage message;
		if (unpacked.startsWith("/") && player.getClan() != null) {
			message = new ChatMessage(colour, size, unpacked.substring(1), effects, player, packed);
			World.getInstance().getClanManager().newClanMessage(player.getClan(), message);
			return;
		}
		message = new ChatMessage(colour, size, unpacked, effects, player, packed);
		player.setLastChatMessage(message);
		player.getUpdateFlags().setChatTextUpdateRequired(true);
	}
}