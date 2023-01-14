package com.rs.entity.actor.player.task.impl;

import com.rs.entity.actor.player.Player;
import com.rs.entity.actor.player.task.Task;
import com.rs.world.World;

import lombok.SneakyThrows;

public final class RunEnergyTask extends Task {
	
	/**
	 * Creates a new {@link RunEnergyTask}.
	 */
	public RunEnergyTask() {
		super(2);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			if((p.getWalkingQueue().isRunToggled() || p.getWalkingQueue().isRunning()) && p.getSprite().getSecondarySprite() != -1) {
				continue;
			} else {
				if(p.playerDetails.getRunEnergy() < 100) {
					p.playerDetails.setRunEnergy(p.playerDetails.getRunEnergy()+1);
				}
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new RunEnergyTask());
	}
}