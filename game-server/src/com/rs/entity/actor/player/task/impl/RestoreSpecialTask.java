package com.rs.entity.actor.player.task.impl;

import com.rs.entity.actor.player.task.Task;
import com.rs.world.World;

public final class RestoreSpecialTask extends Task {
	
	/**
	 * Creates a new {@link RestoreSpecialTask}.
	 */
	public RestoreSpecialTask() {
		super(5);
	}
	
	@Override
	public void execute() {
//		for (Player p : World.getInstance().getPlayerList()) {
//			if (p == null) {
//				continue;
//			}
//			if (p.getSpecialAttack().getSpecialAmount() < 100) {
//				p.getSpecialAttack().setSpecialAmount(p.getSpecialAttack().getSpecialAmount() + 20);
//				if (p.getSpecialAttack().getSpecialAmount() > 100) {
//					p.getSpecialAttack().setSpecialAmount(100);
//				}
//				p.getPlayerDetails().setSpecialAmount(p.getSpecialAttack().getSpecialAmount());
//			}
//		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new RestoreSpecialTask());
	}
}