package com.xeno.model;

import java.util.HashMap;
import java.util.Map;

import com.xeno.content.combat.AttackVars;
import com.xeno.content.combat.Combat.CombatType;
import com.xeno.model.masks.EntityFocus;
import com.xeno.model.masks.Hits;
import com.xeno.model.masks.Sprite;
import com.xeno.model.player.Player;
import com.xeno.util.Misc;

/**
 * An 'entity' in the game world.
 * @author Graham
 *
 */
public abstract class Entity {
	
	private Location location;
	private transient int index;
	private transient Sprite sprite;
	private transient Location teleportTo = null;
	private transient Hits hits;
	private transient boolean dead;
	private transient boolean hidden;
	private transient int combatTurns;
	private transient Entity target;
	private transient Entity attacker;
	private transient Entity lastOpponent;
	private transient long lastAttack;
	private transient long lastAttacked;
	private transient int poisonAmount;
	private transient long lastMagicAttack;
	private transient Map<Entity, Integer> killers;
	protected transient Follow follow;
	private transient CombatType lastCombatType;
	private AttackVars attackVars;
	private transient int lastAttackType;
	private transient int miasmicEffect;
	private transient boolean frozen;
	
	public Entity() {
		this.location = Location.location(2322 + Misc.random(1), 3171 + Misc.random(5), 0);
	}
	
	public Object readResolve() {
		hits = new Hits();
		hidden = false;
		dead = false;
		target = null;
		attacker = null;
		combatTurns = 0;
		poisonAmount = 0;
		killers = new HashMap<Entity, Integer>();
		sprite = new Sprite();
		return this;
	}

	public abstract void hit(int damage);
	public abstract void hit(int damage, Hits.HitType type);
	public abstract int getMaxHp();
	public abstract int getHp();
	public abstract void setHp(int val);
	public abstract void graphics(int id);
	public abstract void graphics(int id, int delay);
	public abstract void graphics(int id, int delay, int height);
	public abstract void animate(int id);
	public abstract void setEntityFocus(int id);
	public abstract void setFaceLocation(Location loc);
	public abstract EntityFocus getEntityFocus();
	public abstract void animate(int id, int delay);
	public abstract boolean isAutoRetaliating();
	public abstract int getAttackAnimation();
	public abstract int getAttackSpeed();
	public abstract int getHitDelay();
	public abstract int getMaxHit();
	public abstract int getDefenceAnimation();
	public abstract int getDeathAnimation();
	public abstract void dropLoot();
	public abstract boolean isDestroyed();
	public abstract void heal(int amt);
	
	public void addToHitCount(Entity killer, int damage) {
		if(!killers.containsKey(killer)) {
			killers.put(killer, damage);
		} else {
			killers.put(killer, killers.get(killer) + damage);
		}
	}
	
	public Entity getKiller() {
		Entity highestHitter = null;
		int highestHit = 0;
		for(Map.Entry<Entity, Integer> entry : killers.entrySet()) {
			if (entry.getKey() != null) {
				if(entry.getValue() > highestHit) {
					highestHitter = entry.getKey();
				}
			}
		}
		return highestHitter;
	}
	
	public void clearKillersHits() {
		killers.clear();
	}
		
	public Hits getHits() {
		return hits;
	}
	
	public void teleport(Location location) {
		this.teleportTo = location;
		if (this instanceof Player) {
			((Player) this).getWalkingQueue().reset();
		}
	}
	
	public void resetTeleportTo() {
		this.teleportTo = null;
	}
	
	public Location getTeleportTo() {
		return this.teleportTo;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setHidden(boolean b) {
		hidden = b;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setDead(boolean b) {
		dead = b;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public int getClientIndex() {
		if(this instanceof Player) {
			return this.index + 32768;
		} else {
			return this.index;
		}
	}
	
	public boolean inCombat() {
		if (target == null && attacker == null) {
			return false;
		}
		return true;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}

	public void setAttacker(Entity attacker) {
		this.attacker = attacker;
	}

	public Entity getAttacker() {
		return attacker;
	}
	
	public void setCombatTurns(int i) {
		combatTurns = i;
	}
	
	public int getCombatTurns() {
		return combatTurns;
	}
	
	public void resetCombatTurns() {
		combatTurns = 0;
	}
	
	public void incrementCombatTurns() {
		combatTurns++;
	}

	public void setLastkiller(Entity lastOpponent) {
		this.lastOpponent = lastOpponent;
	}

	public Entity getLastKiller() {
		return lastOpponent;
	}

	public boolean isPoisoned() {
		return poisonAmount > 0;
	}

	public void setPoisonAmount(int amt) {
		if (amt <= 0) {
			amt = 0;
		}
		if (this instanceof Player) {
			((Player) this).getSettings().setPoisonAmount(amt);
		}
		this.poisonAmount = amt;
	}

	public int getPoisonAmount() {
		return poisonAmount;
	}

	public void setLastAttack(long lastAttack) {
		this.lastAttack = lastAttack;
	}

	public long getLastAttack() {
		return lastAttack;
	}

	public void setFollow(Follow follow) {
		this.follow = follow;
	}

	public Follow getFollow() {
		return follow;
	}

	public void setLastAttackType(int lastAttackType) {
		this.lastAttackType = lastAttackType;
	}

	public int getLastAttackType() {
		return lastAttackType;
	}

	public void setLastMagicAttack(long lastMagicAttack) {
		this.lastMagicAttack = lastMagicAttack;
	}

	public long getLastMagicAttack() {
		return lastMagicAttack;
	}

	public void setLastCombatType(CombatType lastCombatType) {
		this.lastCombatType = lastCombatType;
	}

	public CombatType getLastCombatType() {
		return lastCombatType;
	}
	
	public Sprite getSprites() {
		return sprite;
	}

	public void setLastAttacked(long lastAttacked) {
		this.lastAttacked = lastAttacked;
	}

	public long getLastAttacked() {
		return lastAttacked;
	}

	public void setAttackVars(AttackVars attackVars) {
		this.attackVars = attackVars;
	}

	public AttackVars getAttackVars() {
		return attackVars;
	}

	public void setMiasmicEffect(int miasmicEffect) {
		this.miasmicEffect = miasmicEffect;
	}

	public int getMiasmicEffect() {
		return miasmicEffect;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isFrozen() {
		return frozen;
	}
	
}
