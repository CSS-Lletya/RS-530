package com.xeno.packetbuilder.packets.impl;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = { 23, 244, 81, 196, 199, 234, 168, 166, 64, 53 }, description = "Represents an event where a Player interacting with various interface types")
public class InterfaceInteractionsPacket implements OutgoingPacket {

	private static final int ENTER_AMOUNT = 23;
	private static final int ENTER_TEXT = 244;
	private static final int CLICK_1 = 81;
	private static final int CLICK_2 = 196;
	private static final int CLICK_3 = 124;
	private static final int CLICK_4 = 199;
	private static final int CLICK_5 = 234;
	private static final int CLICK_6 = 168;
	private static final int CLICK_7 = 166;
	private static final int CLICK_8 = 64;
	private static final int CLICK_9 = 53;
	private static final int CLICK_10 = 223;
	@SuppressWarnings("unused")
	private static final int GE_SEARCH = 111;

	@Override
	public void execute(Player player, Packet packet) {
		switch (packet.getId()) {
		case ENTER_AMOUNT:
			
			break;
		case ENTER_TEXT:
			break;

		case CLICK_1:
			break;

		case CLICK_2:
			break;

		case CLICK_3:
			break;

		case CLICK_4:
			break;

		case CLICK_5:
			break;

		case CLICK_6:
			break;

		case CLICK_7:
			break;

		case CLICK_8:
			break;

		case CLICK_9:
			break;

		case CLICK_10:
			break;
		}
	}

	/*
	 * 
	binds>
    <int>23</int> <!-- Enter amount -->
    <int>244</int> <!-- Enter text -->
    <int>81</int> <!-- Option 1 -->
    <int>196</int> <!-- Option 2 -->
    <int>124</int> <!-- Option 3 -->
     <int>199</int> <!-- Option 4 -->
     <int>234</int> <!-- Option 5 -->
     <int>168</int> <!-- Option 6 -->
      <int>166</int> <!-- Option 7 -->
      <int>64</int> <!-- Option 8 -->
      <int>53</int> <!-- Option 9 -->
      <int>111</int> <!-- Grand exchange search -->
	  </binds>
	 */
}