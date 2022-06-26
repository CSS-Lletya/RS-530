package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.World;

public class Kick implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		cmd[1].toLowerCase();
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				if (p.getUsername().toLowerCase().equalsIgnoreCase(cmd[1])) {
					p.getActionSender().logout();
				}
			}
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
