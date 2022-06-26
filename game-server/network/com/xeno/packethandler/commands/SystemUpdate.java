package com.xeno.packethandler.commands;

import com.xeno.entity.player.Player;
import com.xeno.event.impl.SystemUpdateEvent;
import com.xeno.world.World;

public class SystemUpdate implements Command {

	@Override
	public void execute(Player player, String command) {
		if (!World.getInstance().isUpdateInProgress()) {
			World.getInstance().registerEvent(new SystemUpdateEvent());
			World.getInstance().setUpdateInProgress(true);
		}
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
