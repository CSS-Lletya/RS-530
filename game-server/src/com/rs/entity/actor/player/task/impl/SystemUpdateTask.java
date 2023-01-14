package com.rs.entity.actor.player.task.impl;

import com.rs.entity.actor.player.Player;
import com.rs.entity.actor.player.task.Task;
import com.rs.world.World;

import lombok.SneakyThrows;

public final class SystemUpdateTask extends Task {

	private int time = 180;
	
	/**
	 * Creates a new {@link SystemUpdateTask}.
	 */
	public SystemUpdateTask() {
		super(1);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		World.getInstance().players().forEach(p -> p.getActionSender().newSystemUpdate(time));
		if (time-- <= 0) {
			this.stop();
			World.getInstance().players().forEach(Player::forceLogout);
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new SystemUpdateTask());
	}
}