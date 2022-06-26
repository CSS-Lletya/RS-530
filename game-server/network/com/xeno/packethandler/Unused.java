package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;

/**
 * 
 * Unused packets.
 * @author Graham
 */
public class Unused implements PacketHandler {

	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		
	}

}
