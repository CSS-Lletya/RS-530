package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.packethandler.commands.CommandManager;

/**
 * 
 * @author Luke132
 */
public class Command implements PacketHandler {

	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		String command = packet.readRS2String().toLowerCase();
		CommandManager.execute(player, command);
	}

}
