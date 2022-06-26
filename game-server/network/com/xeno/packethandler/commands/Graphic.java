package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

/**
 * 
 * Display graphic command
 * @author Luke132
 */
public class Graphic implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		player.graphics(Integer.valueOf(cmd[1]), 0, 100);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}
}
