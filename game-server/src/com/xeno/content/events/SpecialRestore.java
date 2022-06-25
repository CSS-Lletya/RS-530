package com.xeno.content.events;

import com.xeno.event.Event;
import com.xeno.model.World;
import com.xeno.model.player.Player;

public class SpecialRestore extends Event {

	public SpecialRestore() {
		super(50000);
	}

	@Override
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p == null) {
				continue;
			}
			if (p.getSpecialAttack().getSpecialAmount() < 100) {
				p.getSpecialAttack().setSpecialAmount(p.getSpecialAttack().getSpecialAmount() + 20);
				if (p.getSpecialAttack().getSpecialAmount() > 100) {
					p.getSpecialAttack().setSpecialAmount(100);
				}
				p.getSettings().setSpecialAmount(p.getSpecialAttack().getSpecialAmount());
			}
		}
	}

}
