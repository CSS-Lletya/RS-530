package com.rs.packetbuilder.packets;

import com.rs.entity.actor.player.Player;
import com.rs.net.Packet;

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