package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class RestoreSpecialEvent extends Task {
	
	/**
	 * Creates a new {@link RestoreSpecialEvent}.
	 */
	public RestoreSpecialEvent() {
		super(5);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
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
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new RestoreSpecialEvent());
	}
}