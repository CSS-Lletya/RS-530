package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.World;

public class Yell implements Command {

	@Override
	public void execute(Player player, String command) {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				p.getActionSender().sendMessage(player.getPlayerDetails().getDisplayName() + ": " + command.substring(5));
			}
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
