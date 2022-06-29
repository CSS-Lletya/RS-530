package com.xeno.event.impl;

import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.masks.Hits;
import com.xeno.entity.actor.player.Player;
import com.xeno.event.Event;
import com.xeno.util.Utility;

public class PoisonEvent extends Event {

	private Actor target;
	
	public PoisonEvent(Actor target, int poisonAmount) {
		super(30000 + Utility.random(30000));
		this.target = target;
		initialize(poisonAmount);
	}

	private void initialize(int poisonAmount) { 
		if (target instanceof Player) {
			if (((Player)target).getSettings().getSuperAntipoisonCycles() > 0) {
				stop();
				return;
			}
			((Player)target).getActionSender().sendMessage("You have been poisoned!");
		}
		target.setPoisonAmount(poisonAmount);
	}

	@Override
	public void execute() {
		if (!target.isPoisoned() || target.isDead()) {
			stop();
			return;
		}
		if (target instanceof Player) {
			((Player) target).getActionSender().closeInterfaces();
		}
		target.hit(target.getPoisonAmount(), Hits.HitType.POISON_DAMAGE);
		if (Utility.random(200) >= 100) {
			target.setPoisonAmount(target.getPoisonAmount() - 1);
		}
		if (Utility.random(10) == 0) {
			target.setPoisonAmount(0);
			stop();
			return;
		}
		setTick(30000 + Utility.random(30000));
	}

}
