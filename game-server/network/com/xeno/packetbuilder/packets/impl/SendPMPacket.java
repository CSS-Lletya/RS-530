package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.utility.Utility;

@OutgoingPacketSignature(packetId = 201, description = "Represents an event where a Player private messages another Player")
public class SendPMPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, Packet packet) {
		long name = packet.readLong();
		//int numChars = packet.readByte();
		//String text  = Misc.decryptPlayerChat(packet, numChars);
		byte[] lol = packet.getRemainingData();
		int size = lol.length;
		String text = Utility.textUnpack(lol, size);
		byte[] packed = new byte[size];
		Utility.textPack(packed, text);
		if (text != null && name > 0) {
			player.getFriends().sendMessage(name, text, packed);
		}
	}
}