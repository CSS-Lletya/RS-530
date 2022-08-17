package com.xeno.model.player.skills.magic;

import com.xeno.entity.actor.player.Player;
import com.xeno.utility.DynamicBoolean;
import com.xeno.world.Location;

/**
 * Represents a Magic-based teleporting event, such as magic book teleporting,
 * etc..
 * 
 * @author Dennis
 *
 */
public class MagicTeleporting {

	public static void sendTeleport(Player player, TeleportType type, Location nextLocation) {
		DynamicBoolean teleportingConditions = new DynamicBoolean();
		teleportingConditions.trueArrayCheck(() -> player.move(nextLocation, type),
		(player.isValid()));
	}
}