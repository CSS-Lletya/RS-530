package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = {155, 132, 10}, description = "An Action Button Type 1,2,3 event")
public class ActionButtonPacket implements OutgoingPacket {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, Packet packet) {
		int interfaceId = packet.readShort() & 0xFFFF;
		int buttonId = packet.readShort() & 0xFFFF;
		int buttonId2 = 0;
		if(packet.getLength() >= 6) {
			buttonId2 = packet.readShort() & 0xFFFF;
		}
		if(buttonId2 == 65535) {
			buttonId2 = 0;
		}
		System.out.println(buttonId);
	}
}