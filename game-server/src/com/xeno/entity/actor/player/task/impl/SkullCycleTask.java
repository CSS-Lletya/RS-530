package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class SkullCycleTask extends Task {
	
	/**
	 * Creates a new {@link SkullCycleTask}.
	 */
	public SkullCycleTask() {
		super(6);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				if (p.getPlayerDetails().isSkulled() && !p.getAttributes().exist(Attribute.DEAD)) {
					p.getPlayerDetails().setSkullCycles(p.getPlayerDetails().getSkullCycles() - 1);
				}
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new SkullCycleTask());
	}
}