package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class SystemUpdateEvent extends Task {

	private int time = 180;
	
	/**
	 * Creates a new {@link SystemUpdateEvent}.
	 */
	public SystemUpdateEvent() {
		super(1);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				p.getActionSender().newSystemUpdate(time);
			}
		}
		if (time-- <= 0) {
			this.stop();
			for(Player p : World.getInstance().getPlayerList()) {
				p.getActionSender().forceLogout();
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new SystemUpdateEvent());
	}
}