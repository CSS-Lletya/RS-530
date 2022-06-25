package com.xeno.content.events;

import com.xeno.event.Event;
import com.xeno.model.World;
import com.xeno.model.player.Player;

public class SkullCycle extends Event {

	public SkullCycle() {
		super(60000);
	}

	@Override
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				if (p.getSettings().isSkulled() && !p.isDead()) {
					p.getSettings().setSkullCycles(p.getSettings().getSkullCycles() - 1);
				}
			}
		}
	}

}
