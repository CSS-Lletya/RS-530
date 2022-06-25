package com.xeno.content.events;

import com.xeno.event.Event;
import com.xeno.model.World;
import com.xeno.model.player.Player;

public class LowerPotionCycles extends Event {

	public LowerPotionCycles() {
		super(15000);
	}

	@Override
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null && !p.isDead()) {
				int antifireCycle = p.getSettings().getAntifireCycles();
				int antipoisonCycle = p.getSettings().getSuperAntipoisonCycles();
				if (antifireCycle > 0) {
					if (antifireCycle == 2) {
						p.getActionSender().sendMessage("Your resistance to dragonfire is about to run out!");
					} else if (antifireCycle == 1) {
						p.getActionSender().sendMessage("Your resistance to dragonfire has run out!");
					}
					p.getSettings().setAntifireCycles(antifireCycle - 1);
				}
				if (antipoisonCycle > 0) {
					p.getSettings().setSuperAntipoisonCycles(antipoisonCycle - 1);
				}
			}
		}
	}
}
