package com.xeno.event.impl;

import com.xeno.entity.player.Player;
import com.xeno.event.Event;
import com.xeno.world.World;

/**
 * Handles the regaining of player energy.
 * @author Graham
 *
 */
public class RunEnergyEvent extends Event {

	public RunEnergyEvent() {
		super(2000);
	}

	@Override
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			if((p.getWalkingQueue().isRunToggled() || p.getWalkingQueue().isRunning()) && p.getSprites().getSecondarySprite() != -1) {
				continue;
			} else {
				if(p.getRunEnergy() < 100) {
					p.setRunEnergy(p.getRunEnergy()+1);
				}
			}
		}
	}

}
