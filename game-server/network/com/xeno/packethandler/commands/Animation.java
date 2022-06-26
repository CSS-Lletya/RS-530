package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

/**
 * 
 * Perform animation command.
 * @author Luke132
 */
public class Animation implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		player.animate(Integer.valueOf(cmd[1]));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
