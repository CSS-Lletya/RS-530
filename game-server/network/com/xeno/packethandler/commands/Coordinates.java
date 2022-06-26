package com.xeno.packethandler.commands;

import com.xeno.model.Location;
import com.xeno.model.player.Player;

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
