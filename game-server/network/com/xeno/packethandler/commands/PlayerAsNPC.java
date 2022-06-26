package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

public class PlayerAsNPC implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		player.getAppearance().setNpcId(Integer.valueOf(cmd[1]));
		player.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
