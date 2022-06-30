package com.rs.plugin.standard.listener;

import com.xeno.entity.actor.player.Player;

/**
 * An RS Interface we're going to interact with
 * @author Dennis
 *
 */
public interface RSInterface {
	
	/**
	 * Executes the interface interaction events.
	 * @param player
	 * @param interfaceId
	 * @param componentId
	 * @throws Exception
	 */
	public void execute(Player player, int interfaceId, int button) throws Exception;
}
