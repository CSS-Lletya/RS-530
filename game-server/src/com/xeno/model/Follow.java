package com.xeno.model;

import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;
import com.xeno.util.Misc;

public class Follow {

	private Entity entity;
	private Entity follower;
	private int sameCoordWait;
	
	public Follow(Entity e) {
		this.entity = e;
	}
	Location lastLoc = null;
	public void followEntity() {
		if (follower == null || entity.isDead() || follower.isDead() || follower.isDestroyed()) {
			follower = null;
			return;
		}
		int dir = 0;
		boolean sameCoords = false;
		int x = entity.getLocation().getX();
		int y = entity.getLocation().getY();
		if (follower.getLocation().withinDistance(entity.getLocation(), 1)) {
			return;
		}
		if (follower instanceof Player) {
			dir = ((Player) follower).getWalkingQueue().getLastDirection();
			if (follower.getEntityFocus() == null || follower.getEntityFocus().getEntityId() != entity.getClientIndex()) {
				//entity.setEntityFocus(follower.getClientIndex());
			}
			if (!follower.getLocation().withinDistance(entity.getLocation(), 15)) {
				setFollowing(null);
				return;
			}
			if (entity instanceof Player) {
				((Player) entity).getActionSender().followPlayer(((Player) follower), 1);
				return;
			}
		}
		int targetX = follower.getLocation().getX();
		int targetY = follower.getLocation().getY();
		int newX = x;
		int newY = y;
		if (x == targetX && y == targetY) {
			sameCoordWait++;
			if (sameCoordWait < 2) {
				return;
			}
			if (sameCoordWait == 2) {
				if (Misc.random(3) == 0) {
					newY--;
				} else if (Misc.random(3) == 1) {
					newY++;
				} else if (Misc.random(3) == 2) {
					newX--;
				} else if (Misc.random(3) == 3) {
					newX++;
				}
				sameCoords = true;
				sameCoordWait = 0;
			}
		}
		/*if (dir == 1) { // North.
			targetY--;
		}
		if (dir == 4) { // East.
			targetX--;
		}
		if (dir == 6) { // South.
			targetY++;
		}
		if (dir == 3) { // West.
			targetX++;
		}
		if (dir == 2) { // North east.
			targetY--;
			targetX--;
		}
		if (dir == 7) { // South east.
			targetY++;
			targetX--;
		}
		if (dir == 5) { // South west.
			targetY++;
			targetX++;
		}
		if (dir == 0) { // North west.
			targetY--;
			targetX++;
		}*/
		if (!sameCoords) {
			if (targetX > x && targetY == y) {
				newX++;
			} else
			if (targetX < x && targetY == y) {
				newX--;
			}else
			if (targetX == x && targetY > y) {
				newY++;
			}else
			if (targetX == x && targetY < y) {
				newY--;
			}else
			if (targetX > x && targetY > y) {
				newX++;
				newY++;
			}else
			if (targetX < x && targetY < y) {
				newX--;
				newY--;
			}else
			if (targetX > x && targetY < y) {
				newX++;
				newY--;
			}else
			if (targetX < x && targetY > y) {
				newX--;
				newY++;
			}
		}
		//if (entity.getLocation().withinDistance(following.getLocation(),  Combat.npcUsesRange((NPC)entity) ? 30 : Combat.getNPCSize(entity, following))) {
		//	return;
		//}
		if (entity instanceof NPC) {
			Location newLoc = Location.location(newX, newY, entity.getLocation().getZ());
			if (!newLoc.inArea(((NPC)entity).getMinimumCoords().getX(), ((NPC)entity).getMinimumCoords().getY(), ((NPC)entity).getMaximumCoords().getX(), ((NPC)entity).getMaximumCoords().getY())) {
				follower = null;
				return;
			}
			if (!sameCoords && newX == targetX && newY == targetY) {
				return;
			}
		}
		if (follower.getEntityFocus() == null || follower.getEntityFocus().getEntityId() != entity.getClientIndex()) {
			entity.setEntityFocus(follower.getClientIndex());
		}
		/*if (!npc.getLocation().withinDistance(following.getLocation())) {
			if (((Player) following).getSettings().getSummonedNPC() == npc) {
				npc.animate(8298);
				npc.graphics(NPCSizes.getNpcSize(npc.getId()) > 0 ? 1315 : 1314);
				npc.setLocation(Location.location(targetX, targetY, following.getLocation().getZ()));
				npc.setEntityFocus(following.getClientIndex());
				return;
				
			}
		}*/
		/*if (entity instanceof NPC) {
			((Player) entity).getActionSender().followPlayer(((NPC) follower), 1);
		((NPC)entity).getSprites().setSprites(Misc.direction(x, y, newX, newY), -1);
			if (((NPC)entity).getSprites().getPrimarySprite() != -1) {
				int sprite = ((NPC)entity).getSprites().getPrimarySprite() >> 1;
				((NPC)entity).getSprites().setSprites(sprite, -1);
				((NPC)entity).setLocation(Location.location(newX, newY, ((NPC)entity).getLocation().getZ()));
			}
		} else {
		}*/
	}
	
	public Entity getFollowing() {
		return follower;
	}

	public void setFollowing(Entity following) {
		if (following == null) {
			if (entity instanceof Player) {
				((Player) entity).getActionSender().followPlayer(null, -1);
			}
		}
		this.follower = following;
	}
}
