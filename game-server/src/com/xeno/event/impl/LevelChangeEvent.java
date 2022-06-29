package com.xeno.event.impl;

import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.event.Event;
import com.xeno.model.player.skills.Skills;
import com.xeno.world.World;

public class LevelChangeEvent extends Event {

	private static int status;
	
	public LevelChangeEvent() {
		super(22000);
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
				if (p.getLevels().getLevel(i) < p.getLevels().getLevelForXp(i)) {
					p.getLevels().setLevel(i, p.getLevels().getLevel(i) + 1);
					updated = true;
				} else if (p.getLevels().getLevel(i) > p.getLevels().getLevelForXp(i) && i != 3 && status == 1) { // status == 1 so stats DONT go down 2x faster.
					p.getLevels().setLevel(i, p.getLevels().getLevel(i) - 1);
					updated = true;
				}
			}
			if (updated) {
				p.getActionSender().sendSkillLevels();
			}
		}
		if (status == 1) {
			for (NPC n : World.getInstance().getNpcList()) {
				if (n.getHp() < n.getMaxHp() && !n.isDead() && !n.inCombat()) {
					n.heal(1);
				}
			}
		}
		status = status == 0 ? 1 : 0;
	}
}
