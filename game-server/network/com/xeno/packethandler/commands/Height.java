package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.Location;

public class Height implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		Location tele = null;
		try {
			tele  = Location.location(player.getLocation().getX(), player.getLocation().getY(), Integer.parseInt(cmd[1]));
		} catch (Exception e) {
			tele = Location.location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		}
		player.teleport(tele);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
