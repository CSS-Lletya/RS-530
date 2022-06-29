package com.xeno.content;

import com.xeno.entity.actor.player.Player;
import com.xeno.event.AreaEvent;
import com.xeno.event.Event;
import com.xeno.util.Utility;
import com.xeno.world.Location;
import com.xeno.world.World;
import com.xeno.world.WorldObject;

public class WildernessObelisks {

	private static final int ACTIVATED_ID = 14825;
	
	private static boolean[] obeliskActivated = new boolean[6];
	
	/*
	 * These are the MIDDLE of the platform, since the activators are all 2 squares from the center.
	 */
	private static final int[][] OBELISK_LOCATIONS = {
		{3035, 3732, 0}, // 27 wild
		{2980, 3866, 0}, // 44 wild
		{3156, 3620, 0}, // 13 wild
		{3219, 3656, 0}, // 18 wild
		{3307, 3916, 0}, // 50 wild
		{3106, 3794, 0}, // 35 wild
	};
	
	private static final int[] OBELISK_ID = {
		14827, // 27 wild
		14826, // 44 wild
		14829, // 13 wild
		14830, // 18 wild
		14831, // 50 wild
		14828, // 35 wild
	};
	
	public WildernessObelisks() {
		
	}
	
	public static boolean useWildernessObelisk(Player p, int id, Location loc) {
		for(int i = 0; i < OBELISK_ID.length; i++) {
			if (id == OBELISK_ID[i]) {
				if (loc.inArea(OBELISK_LOCATIONS[i][0] - 2, OBELISK_LOCATIONS[i][1] - 2, OBELISK_LOCATIONS[i][0] + 2, OBELISK_LOCATIONS[i][1] + 2)) {
					final int index = i;
					World.getInstance().registerCoordinateEvent(new AreaEvent(p, loc.getX() - 1, loc.getY() - 1, loc.getX() + 1, loc.getY() + 1) {
		
						@Override
						public void run() {
							activateObelisk(index);
						}				
					});
				}
				return true;
			}
		}
		return false;
	}

	private static void activateObelisk(final int index) {
		if (obeliskActivated[index]) {
			return;
		}
		final Location[] obeliskLocations = getLocations(index);
		for (int i = 0; i < 4; i++) {
			WorldObject obj = new WorldObject(OBELISK_ID[index], ACTIVATED_ID, obeliskLocations[i], 0, 10);
			obj.setSecondForm(true);
			World.getInstance().getGlobalObjects().add(obj);
			for (Player p : World.getInstance().getPlayerList()) {
				p.getActionSender().createObject(ACTIVATED_ID, obeliskLocations[i], 0, 10);
			}
		}
		obeliskActivated[index] = true;
		World.getInstance().registerEvent(new Event(4000 + (Utility.random(4)) * 1000) {

			@Override
			public void execute() {
				this.stop();
				int randomOb = index;
				while(randomOb == index) {
					// While loop so if the random one is the same one, it picks a new one
					randomOb = (int) (Math.random() * OBELISK_ID.length);
				}
				final int random = randomOb;
				for (Player p : World.getInstance().getPlayerList()) {
					if (p != null) {
						if (p.getLocation().inArea(OBELISK_LOCATIONS[index][0] - 2, OBELISK_LOCATIONS[index][1] - 2, OBELISK_LOCATIONS[index][0] + 2, OBELISK_LOCATIONS[index][1] + 2)) {
							// TODO get the big purple graphic
							p.graphics(1690);
							p.animate(8939);
							final Player p2 = p;
							World.getInstance().registerEvent(new Event(1200) {

								@Override
								public void execute() {
									this.stop();
									p2.teleport(Location.location((OBELISK_LOCATIONS[random][0] - 1) + Utility.random(2), (OBELISK_LOCATIONS[random][1] - 1) + Utility.random(2), 0));
									World.getInstance().registerEvent(new Event(500) {

										@Override
										public void execute() {
											this.stop();
											p2.animate(8941);
										}
									});
								}	
							});
						}
					}
				}
				for (int i = 0; i < 4; i++) {
					WorldObject obj = World.getInstance().getGlobalObjects().getObject(OBELISK_ID[index], obeliskLocations[i]);
					World.getInstance().getGlobalObjects().restoreObject(obj);
				}
				obeliskActivated[index] = false;
			}
		});
	}

	private static Location[] getLocations(int index) {
		Location[] loc = new Location[4];
		int x = OBELISK_LOCATIONS[index][0];
		int y = OBELISK_LOCATIONS[index][1];
		loc[0] = Location.location(x - 2, y - 2, 0); // SW
		loc[1] = Location.location(x - 2, y + 2, 0); // NW
		loc[2] = Location.location(x + 2, y + 2, 0); // NE
		loc[3] = Location.location(x + 2, y - 2, 0); // SE
		return loc;
	}
}
