package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.Location;

public class Coordinates implements Command {

	@Override
	public void execute(Player player, String command) {
		Location loc = player.getLocation();
		player.getActionSender().sendMessage("X = " + loc.getX() + " Y = " + loc.getY());
		
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
