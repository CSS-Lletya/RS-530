package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class LowerPotionCyclesTask extends Task {
	
	/**
	 * Creates a new {@link LowerPotionCyclesTask}.
	 */
	public LowerPotionCyclesTask() {
		super(15);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null && !p.isDead()) {
				int antifireCycle = p.getPlayerDetails().getAntifireCycles();
				int antipoisonCycle = p.getPlayerDetails().getSuperAntipoisonCycles();
				if (antifireCycle > 0) {
					if (antifireCycle == 2) {
						p.getActionSender().sendMessage("Your resistance to dragonfire is about to run out!");
					} else if (antifireCycle == 1) {
						p.getActionSender().sendMessage("Your resistance to dragonfire has run out!");
					}
					p.getPlayerDetails().setAntifireCycles(antifireCycle - 1);
				}
				if (antipoisonCycle > 0) {
					p.getPlayerDetails().setSuperAntipoisonCycles(antipoisonCycle - 1);
				}
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new LowerPotionCyclesTask());
	}
}