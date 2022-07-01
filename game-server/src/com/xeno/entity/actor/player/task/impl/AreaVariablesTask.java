package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.Location;
import com.xeno.entity.actor.attribute.Attribute;
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
				if (!p.getAttributes().exist(Attribute.IN_WILDERNESS)) {
					p.getActionSender().sendPlayerOption("Attack", 1, 1);
					p.getInterfaceManager().sendOverlay(381);
					p.getAttributes().get(Attribute.IN_WILDERNESS).set(true);
				}
			} else {
				if (p.getAttributes().exist(Attribute.IN_WILDERNESS)) {
					p.getActionSender().sendPlayerOption("null", 1, 1);
					p.getInterfaceManager().sendRemoveOverlay();
					p.setLastWildLevel(0);
					p.getAttributes().get(Attribute.IN_WILDERNESS).set(false);
				}
			}
		}
		if (Area.inMultiCombat(p.getLocation())) {
			if (!p.getAttributes().exist(Attribute.IN_MULTI_ZONE)) {
				p.getInterfaceManager().displayMultiIcon();
				p.getAttributes().get(Attribute.IN_MULTI_ZONE).set(true);
			}
		} else {
			if (p.getAttributes().exist(Attribute.IN_MULTI_ZONE)) {
				p.getInterfaceManager().removeMultiIcon();
				p.getAttributes().get(Attribute.IN_MULTI_ZONE).set(false);
			}
		}
	}
}