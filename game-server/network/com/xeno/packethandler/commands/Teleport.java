package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.Location;

/**
 * 
 * Teleport command.
 * @author Luke132
 */
public class Teleport implements Command {

	@Override
	public void execute(Player player, String command) {
	String cmd[] = command.split(" ");
		Location tele = null;
		try {
			tele = Location.location(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
		} catch (Exception e) {
			tele = Location.location(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), 0);
		}
		player.teleport(tele);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
