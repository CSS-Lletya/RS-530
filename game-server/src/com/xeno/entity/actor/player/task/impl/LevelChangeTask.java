package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.model.player.skills.Skills;
import com.xeno.world.World;

public final class LevelChangeTask extends Task {
	
	private static int status;
	
	/**
	 * Creates a new {@link LevelChangeTask}.
	 */
	public LevelChangeTask() {
		super(2, false);
		status = 0;
	}

	@Override
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			boolean updated = false;
			boolean rapidHeal = p.getPrayers().isRapidHeal();
			boolean rapidRestore = p.getPrayers().isRapidRestore();
			if (p.isDead() || p == null || (status == 0 && !rapidHeal && !rapidRestore)) {
				continue;
			}
			for (int i = 0; i < Skills.SKILL_COUNT; i++) {
				if (i == 5 || (i == 3 && p.inCombat())) {
					continue;
				} else if (status == 0) {
					if ((rapidHeal && !rapidRestore && i != 3) || (!rapidHeal && rapidRestore && i == 3)) {
						continue;
					}
				}
				if (p.getSkills().getLevel(i) < p.getSkills().getLevelForXp(i)) {
					p.getSkills().setLevel(i, p.getSkills().getLevel(i) + 1);
					updated = true;
				} else if (p.getSkills().getLevel(i) > p.getSkills().getLevelForXp(i) && i != 3 && status == 1) { // status == 1 so stats DONT go down 2x faster.
					p.getSkills().setLevel(i, p.getSkills().getLevel(i) - 1);
					updated = true;
				}
			}
			if (updated) {
				p.getActionSender().sendSkillLevels();
			}
		}
		if (status == 1) {
			World.getInstance().npcs().filter(n -> n.getHp() < n.getMaxHp() && !n.isDead() && !n.inCombat()).forEach(n -> n.heal(1));
		}
		status = status == 0 ? 1 : 0;
	}
	
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new LevelChangeTask());
	}
}