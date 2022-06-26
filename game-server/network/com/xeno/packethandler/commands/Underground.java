package com.xeno.packethandler.commands;

import com.xeno.model.Location;
import com.xeno.model.player.Player;

public class Underground implements Command {

	@Override
	public void execute(Player player, String command) {
		player.teleport(Location.location(player.getLocation().getX(), player.getLocation().getY() + 6400, 0));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
