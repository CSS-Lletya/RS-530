package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class SkullCycleEvent extends Task {
	
	/**
	 * Creates a new {@link SkullCycleEvent}.
	 */
	public SkullCycleEvent() {
		super(6);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				if (p.getSettings().isSkulled() && !p.isDead()) {
					p.getSettings().setSkullCycles(p.getSettings().getSkullCycles() - 1);
				}
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new SkullCycleEvent());
	}
}