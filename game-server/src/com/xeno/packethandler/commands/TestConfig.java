package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;

public class TestConfig implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		player.getActionSender().sendConfig(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
