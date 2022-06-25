package com.xeno.util;

import com.xeno.event.AreaEvent;
import com.xeno.event.Event;
import com.xeno.model.Location;
import com.xeno.model.World;
import com.xeno.model.masks.ForceMovement;
import com.xeno.model.player.Player;

public class Area {

	//TODO change all this to polygons or some shit so we dont just have to use squares
	
	public Area() {
		
	}
	
	public static boolean inWilderness(Location l) {
		return l.inArea(2945, 3524, 3391, 3975);
	}

	public static boolean atDuelArena(Location l) {
		return l.inArea(3318, 3247, 3327, 3247) ||
		l.inArea(3324, 3247, 3328, 3264) ||
		l.inArea(3327, 3262, 3342, 3270) ||
		l.inArea(3342, 3262, 3387, 3280) ||
		l.inArea(3387, 3262, 3394, 3271) ||
		l.inArea(3313, 3224, 3325, 3247) ||
		l.inArea(3326, 3200, 3398, 3267); // Entire arena
	}

	public static boolean atBarrows(Location l) {
		return l.inArea(3521, 9663, 3582, 9727);
	}

	public static boolean inMultiCombat(Location l) {
		return atGodwars(l) || inTzHaar(l) || inFightPits(l) || inFightCave(l);
	}
	
	public static boolean inFightCave(Location l) {
		return l.getX() >= 19000;
	}

	public static boolean atGodwars(Location l) {
		return l.inArea(2820, 5245, 2964, 5380);
	}
	
	public static boolean atAgilityArena(Location l) {
		return l.inArea(2757, 9542, 2809, 9594);
	}
	
	public static boolean inFightPits(Location l) {
		return l.inArea(2376, 5127, 2422, 5168);
	}
	
	public static boolean inFightPitsWaitingArea(Location l) {
		return l.inArea(2394, 5169, 2404, 5175);
	}
	
	public static boolean inTzHaar(Location l) {
		return l.inArea(2370, 5120, 2541, 5185);
	}
	
	public static boolean onWaterbirthIsle(Location l) {
		return l.inArea(2494, 3710, 2564, 3786);
	}

	public static void crossDitch(final Player p, final int x, final int y) {
		if (p.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		World.getInstance().registerCoordinateEvent(new AreaEvent(p, x, y - 1, x, y + 2) {

			@Override
			public void run() {
				p.getActionSender().closeInterfaces();
				p.getWalkingQueue().reset();
				p.setTemporaryAttribute("unmovable", true);
				final int newY = p.getLocation().getY() >= 3523 ? p.getLocation().getY()-3 : p.getLocation().getY()+3;
				final int dir = newY == 3 ? 0 : 4;
				Location faceLocation = Location.location(p.getLocation().getX(), dir == 3 ? 3523 : 3520, 0);
				p.setFaceLocation(faceLocation);
				World.getInstance().registerEvent(new Event(500) {
					@Override
					public void execute() {
						this.stop();
						p.animate(6132);
						int regionX = p.getUpdateFlags().getLastRegion().getRegionX();
						int regionY = p.getUpdateFlags().getLastRegion().getRegionY();
						int lX = (p.getLocation().getX() - ((regionX - 6) * 8));
						int lY = (p.getLocation().getY() - ((regionY - 6) * 8));
						ForceMovement movement = new ForceMovement(lX, lY, lX, newY, 33, 60, dir);
						p.setForceMovement(movement);		
						p.setFaceLocation(Location.location(x, y, 0));
						World.getInstance().registerEvent(new Event(1250) {

							@Override
							public void execute() {
								this.stop();
								int playerY = p.getLocation().getY();
								int nY = playerY >= 3523 ? 3520 : 3523;
								p.teleport(Location.location(p.getLocation().getX(), nY, 0));
								p.removeTemporaryAttribute("unmovable");
							}	
						});
					}
				});
			}
		});
	}
}
