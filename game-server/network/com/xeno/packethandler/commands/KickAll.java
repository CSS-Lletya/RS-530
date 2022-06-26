package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.world.World;

public class KickAll implements Command {

	@Override
	public void execute(Player player, String command) {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				p.getActionSender().forceLogout();
			}
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
