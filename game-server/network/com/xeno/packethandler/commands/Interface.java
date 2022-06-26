package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

public class Interface implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		player.getActionSender().displayInterface(Integer.parseInt(cmd[1]));
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
