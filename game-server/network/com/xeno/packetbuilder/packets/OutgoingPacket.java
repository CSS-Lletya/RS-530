package com.xeno.packetbuilder.packets;

import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;

/**
 * Represents an Outgoing Packet
 * @author Dennis
 *
 */
public interface OutgoingPacket {
	
	/**
	 * Executes the Packet
	 * @param player
	 * @param entity
	 * @throws Exception
	 */
	public void execute(Player player, Packet packet);
}