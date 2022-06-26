package com.xeno.packethandler.commands;

import com.xeno.content.ConfigureAppearance;
import com.xeno.entity.player.Player;

public class CharacterAppearance implements Command {

	@Override
	public void execute(Player player, String command) {
		ConfigureAppearance.openInterface(player);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
