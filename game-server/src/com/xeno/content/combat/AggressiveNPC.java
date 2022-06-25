package com.xeno.content.combat;

import com.xeno.event.Event;
import com.xeno.model.World;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;
import com.xeno.util.Area;

public class AggressiveNPC extends Event {
	
	public AggressiveNPC() {
		super(750);
	}

	@Override
	public void execute() {
		for (NPC npc : World.getInstance().getNpcList()) {
			if (!npc.getDefinition().isAggressive()) {
				continue;
			}
			for (Player p : World.getInstance().getPlayerList()) {
				if (p != null && npc != null) {
					if (p.getLocation().getZ() == npc.getLocation().getZ()) {
						if (p.getLocation().inArea(npc.getMinimumCoords().getX(), npc.getMinimumCoords().getY(), npc.getMaximumCoords().getX(), npc.getMaximumCoords().getY())) {
							if (p.getLocation().inArea(npc.getLocation().getX() - 3, npc.getLocation().getY() - 3, npc.getLocation().getX() + 3, npc.getLocation().getY() + 3)) {
								if (!npc.isDead() && !npc.inCombat() && !npc.isDestroyed() && !npc.isHidden() && !p.inCombat() && ((npc.getDefinition().getCombat() >= (p.getLevels().getCombatLevel() * 2)) || Area.inWilderness(p.getLocation()))) {
									npc.setTarget(p);
									npc.setEntityFocus(p.getClientIndex());
									npc.getFollow().setFollowing(p);
								}
							}
						}
					}
				}
			}
		}
	}

}
