package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.net.entity.masks.Hits;
import com.xeno.utility.Utility;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class PoisonTask extends Task {
	
	private Actor target;
	private int poisonAmount;
	/**
	 * Creates a new {@link PoisonTask}.
	 */
	public PoisonTask(Actor target, int poisonAmount) {
		super(30);
		this.target = target;
		this.poisonAmount = poisonAmount;
		initialize(poisonAmount);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		if (!target.isPoisoned() || target.isDead()) {
			stop();
			return;
		}
		if (target instanceof Player) {
			((Player) target).getInterfaceManager().closeInterfaces();
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
		setDelay(30);
	}
	
	private void initialize(int poisonAmount) { 
		if (target instanceof Player) {
//			if (((Player)target).getPlayerDetails().getSuperAntipoisonCycles() > 0) {
//				stop();
//				return;
//			}
			((Player)target).getActionSender().sendMessage("You have been poisoned!");
		}
		target.setPoisonAmount(poisonAmount);
	}

	
	@Override
	public void onCancel() {
		World.getInstance().submit(new PoisonTask(target, poisonAmount));
	}
}