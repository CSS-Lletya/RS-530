package com.xeno.packetbuilder.packets.impl;

import java.util.stream.IntStream;

import com.rs.plugin.standard.RSInterfacePluginDispatcher;
import com.xeno.GameConstants;
import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;

@OutgoingPacketSignature(packetId = {155, 132, 10}, description = "An Action Button Type 1,2,3 event")
public class ActionButtonPacket implements OutgoingPacket {

	private int slot = 0;
	
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
		slot = buttonId2;
		if (GameConstants.DEBUG_MODE)
			LogUtility.log(LogType.INFO, "Inter: "+ interfaceId + " - button: " + buttonId + " button type 2: " + buttonId2);
		if (player.getAttributes().get(Attribute.LOCKED).getBoolean())
			return;
		if (IntStream.of(3).anyMatch(id -> id == interfaceId))
			player.getInterfaceManager().closeChatboxInterface();
		player.getMapZoneManager().executeVoid(player, zone -> zone.processButtonClick(player, interfaceId, buttonId, slot));
		RSInterfacePluginDispatcher.execute(player, interfaceId, buttonId, buttonId2);
	}
}