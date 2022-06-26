package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;

public class EmptyInventory implements Command {

	@Override
	public void execute(Player player, String command) {
		player.getInventory().deleteAll();
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
