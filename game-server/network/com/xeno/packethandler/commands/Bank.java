package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

public class Bank implements Command {

	@Override
	public void execute(Player player, String command) {
		if (!player.inCombat()) {
			player.getBank().openBank();
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
