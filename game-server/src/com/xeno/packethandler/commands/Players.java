package com.xeno.packethandler.commands;

import com.xeno.model.World;
import com.xeno.model.player.Player;

public class Players implements Command {

	@Override
	public void execute(Player player, String command) {
		player.getActionSender().sendMessage("Players online : " + World.getInstance().getPlayerList().size());
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
