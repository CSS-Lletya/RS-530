package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;

public class RestoreSpecial implements Command {

	@Override
	public void execute(Player player, String command) {
		player.getSpecialAttack().resetSpecial();
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}
}
