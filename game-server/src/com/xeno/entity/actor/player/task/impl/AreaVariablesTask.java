package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.Location;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.utility.Area;
import com.xeno.world.World;

public final class AreaVariablesTask extends Task {
	
	/**
	 * Creates a new {@link DrainPrayerTask}.
	 */
	public AreaVariablesTask() {
		super(1, false);
	}
	
	@Override
	public void execute() {
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				updateVariables(p);
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.getInstance().submit(new AreaVariablesTask());
	}
	
	public int wildernessLevel(Location l) {
		int y = l.getY();
		if (!Area.inWilderness(l)) {
			return -1;
		}
		if(y > 3523 && y < 4000) {
			return (((int)(Math.ceil((double)(y)-3520D) / 8D) + 1));
		}
		return -1;
	}
	
	/*
	 * NOTE: Anything that goes in here and varies between HD and LD, 
	 * reset the variable in ActionSender.configureGameScreen
	 */
	public void updateVariables(Player p) {
		int currentLevel = wildernessLevel(p.getLocation());
		if (currentLevel != p.getLastWildLevel()) {
			if (currentLevel > 0) {
				p.setLastWildLevel(currentLevel);
				if (p.getTemporaryAttribute("inWild") == null) {
					p.getActionSender().sendPlayerOption("Attack", 1, 1);
					p.getInterfaceManager().sendOverlay(381);
					p.setTemporaryAttribute("inWild", true);
				}
			} else {
				if (p.getTemporaryAttribute("inWild") != null) {
					p.getActionSender().sendPlayerOption("null", 1, 1);
					p.getInterfaceManager().sendRemoveOverlay();
					p.setLastWildLevel(0);
					p.removeTemporaryAttribute("inWild");
				}
			}
		}
		if (Area.inMultiCombat(p.getLocation())) {
			if (p.getTemporaryAttribute("inMulti") == null) {
				p.getInterfaceManager().displayMultiIcon();
				p.setTemporaryAttribute("inMulti", true);
			}
		} else {
			if (p.getTemporaryAttribute("inMulti") != null) {
				p.getInterfaceManager().removeMultiIcon();
				p.removeTemporaryAttribute("inMulti");
			}
		}
	}
}