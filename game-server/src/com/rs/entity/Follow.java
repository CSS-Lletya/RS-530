package com.rs.entity;

import com.rs.entity.actor.Actor;
import com.rs.entity.actor.attribute.Attribute;
import com.rs.entity.actor.npc.NPC;
import com.rs.entity.actor.player.Player;
import com.rs.utility.RandomUtils;
import com.rs.world.Location;

public class Follow {

	private Actor entity;
	private Actor follower;
	private int sameCoordWait;

	public Follow(Actor e) {
		this.entity = e;
	}

	Location lastLoc = null;

	public void followEntity() {
		if (follower == null || entity.getAttributes().get(Attribute.DEAD).getBoolean() || follower.getAttributes().get(Attribute.DEAD).getBoolean() || follower.isValid()) {
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
			if (follower.getEntityFocus() == null
					|| follower.getEntityFocus().getEntityId() != entity.getClientIndex()) {
				// entity.setEntityFocus(follower.getClientIndex());
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
				if (RandomUtils.random(3) == 0) {
					newY--;
				} else if (RandomUtils.random(3) == 1) {
					newY++;
				} else if (RandomUtils.random(3) == 2) {
					newX--;
				} else if (RandomUtils.random(3) == 3) {
					newX++;
				}
				sameCoords = true;
				sameCoordWait = 0;
			}
		}
		if (!sameCoords) {
			if (targetX > x && targetY == y) {
				newX++;
			} else if (targetX < x && targetY == y) {
				newX--;
			} else if (targetX == x && targetY > y) {
				newY++;
			} else if (targetX == x && targetY < y) {
				newY--;
			} else if (targetX > x && targetY > y) {
				newX++;
				newY++;
			} else if (targetX < x && targetY < y) {
				newX--;
				newY--;
			} else if (targetX > x && targetY < y) {
				newX++;
				newY--;
			} else if (targetX < x && targetY > y) {
				newX--;
				newY++;
			}
		}
		if (entity instanceof NPC) {
			Location newLoc = Location.location(newX, newY, entity.getLocation().getZ());
			if (!newLoc.inArea(((NPC) entity).getMinimumCoords().getX(), ((NPC) entity).getMinimumCoords().getY(),
					((NPC) entity).getMaximumCoords().getX(), ((NPC) entity).getMaximumCoords().getY())) {
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
	}

	public Actor getFollowing() {
		return follower;
	}

	public void setFollowing(Actor following) {
		if (following == null) {
			if (entity instanceof Player) {
				((Player) entity).getActionSender().followPlayer(null, -1);
			}
		}
		this.follower = following;
	}
}
