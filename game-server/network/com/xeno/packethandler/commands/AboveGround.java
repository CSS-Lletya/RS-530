package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.Location;

public class AboveGround implements Command {

	@Override
	public void execute(Player player, String command) {
		player.teleport(Location.location(player.getLocation().getX(), player.getLocation().getY() - 6400, 0));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
