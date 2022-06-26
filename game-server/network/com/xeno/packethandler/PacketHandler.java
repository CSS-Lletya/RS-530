package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;

/**
 * Packet handler interface.
 * @author Graham
 *
 */
public interface PacketHandler {
	
	public void handlePacket(Player player, IoSession session, Packet packet);

}
