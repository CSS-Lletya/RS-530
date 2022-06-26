package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

public class TeleToPlayer implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
