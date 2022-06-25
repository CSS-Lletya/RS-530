package com.xeno.packethandler.commands;

import com.xeno.model.World;
import com.xeno.model.player.Player;

public class SpawnObject implements Command {

	@Override
	public void execute(final Player player, String command) {
		String cmd[] = command.split(" ");
		//player.getActionSender().createObject(Integer.parseInt(cmd[1]), player.getLocation(), 0, 10);
		for (Player p : World.getInstance().getPlayerList()) {
			p.getActionSender().createObject(Integer.parseInt(cmd[1]), player.getLocation(), Integer.parseInt(cmd[2]), 10);
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
