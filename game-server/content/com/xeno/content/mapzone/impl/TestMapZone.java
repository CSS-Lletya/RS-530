package com.xeno.content.mapzone.impl;

import com.xeno.content.mapzone.MapZone;
import com.xeno.content.mapzone.ZoneRestriction;
import com.xeno.entity.actor.player.Player;

public class TestMapZone extends MapZone {

	public TestMapZone() {
		super("TEST", MapZoneSafetyCondition.SAFE, MapZoneType.NORMAL, ZoneRestriction.FIRES, ZoneRestriction.CANNON);
	}

	@Override
	public void start(Player player) {
		System.out.println(getMapeZoneName());
	}

	@Override
	public void finish(Player player) {
		
	}
}