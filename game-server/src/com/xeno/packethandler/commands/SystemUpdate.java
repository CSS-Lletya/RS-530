package com.xeno.packethandler.commands;

import com.xeno.content.events.SystemUpdateEvent;
import com.xeno.model.World;
import com.xeno.model.player.Player;

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
