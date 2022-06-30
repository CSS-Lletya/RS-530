package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.model.player.skills.prayer.Prayer;
import com.xeno.world.World;

import lombok.SneakyThrows;

public final class DrainPrayerTask extends Task {
	
	/**
	 * Creates a new {@link DrainPrayerTask}.
	 */
	public DrainPrayerTask() {
		super(1);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p == null || p.isDead() || !Prayer.isPrayerActive(p)) {
				this.stop();
				return;
			}
			double amountDrain = 0.0;
			for(int i = 0; i < p.getPrayers().getPrayerActiveArray().length ; i++) {
				if(p.getPrayers().isPrayerActive(i)) {
					double drain = Prayer.DRAIN_RATE[i];
					double bonus = (0.035 * p.getBonuses().getBonus(12));
					drain = drain * (1 + bonus);
					drain = 0.6 / drain;
					amountDrain += drain;
				}
			}
			p.getPlayerDetails().decreasePrayerPoints(amountDrain);
			if(p.getPlayerDetails().getPrayerPoints() <= 0) {
				this.stop();
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new DrainPrayerTask());
	}
}