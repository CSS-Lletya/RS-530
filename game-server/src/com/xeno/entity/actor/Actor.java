package com.xeno.entity.actor;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import com.xeno.content.combat.Combat.CombatType;
import com.xeno.content.combat.constants.AttackVars;
import com.xeno.entity.Entity;
import com.xeno.entity.EntityType;
import com.xeno.entity.Follow;
import com.xeno.entity.Location;
import com.xeno.entity.actor.masks.EntityFocus;
import com.xeno.entity.actor.masks.Hits;
import com.xeno.entity.actor.masks.Sprite;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.utility.Utility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Actor extends Entity {
	
	public Actor() {
		this.location = Location.location(2322 + Utility.random(1), 3171 + Utility.random(5), 0);
	}
	
	protected transient Sprite sprite;
	protected transient Location teleportTo = null;
	protected transient Hits hits;
	protected transient boolean dead;
	protected transient boolean hidden;
	protected transient int combatTurns;
	protected transient Actor target;
	protected transient Actor attacker;
	protected transient Actor lastOpponent;
	protected transient long lastAttack;
	protected transient long lastAttacked;
	protected transient int poisonAmount;
	protected transient long lastMagicAttack;
	protected transient Map<Actor, Integer> killers;
	protected transient Follow follow;
	protected transient CombatType lastCombatType;
	protected AttackVars attackVars;
	protected transient int lastAttackType;
	protected transient int miasmicEffect;
	protected transient boolean frozen;
	
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

	public Object readResolve(EntityType type) {
		this.type = requireNonNull(type);
		hits = new Hits();
		hidden = false;
		dead = false;
		target = null;
		attacker = null;
		combatTurns = 0;
		poisonAmount = 0;
		killers = new HashMap<Actor, Integer>();
		sprite = new Sprite();
		return this;
	}
	
	/**
	 * The type of node that this node is.
	 */
	@Getter
	private EntityType type;

	/**
	 * Determines if this entity is a {@link Player}.
	 * 
	 * @return {@code true} if this entity is a {@link Player}, {@code false}
	 *         otherwise.
	 */
	public final boolean isPlayer() {
		return getType() == EntityType.PLAYER;
	}

	/**
	 * Executes the specified action if the underlying node is a player.
	 * 
	 * @param action the action to execute.
	 */
	public final void ifPlayer(Consumer<Player> action) {
		if (!this.isPlayer()) {
			return;
		}
		action.accept(this.toPlayer());
	}

	/**
	 * Casts the {@link Actor} to a {@link Player}.
	 * 
	 * @return an instance of this {@link Actor} as a {@link Player}.
	 */
	public final Player toPlayer() {
		Preconditions.checkArgument(isPlayer(), "Cannot cast this entity to player.");
		return (Player) this;
	}

	/**
	 * Determines if this entity is a {@link Mob}.
	 * 
	 * @return {@code true} if this entity is a {@link Mob}, {@code false}
	 *         otherwise.
	 */
	public final boolean isNPC() {
		return getType() == EntityType.NPC;
	}

	/**
	 * Executes the specified action if the underlying node is a player.
	 * 
	 * @param action the action to execute.
	 */
	public final void ifNpc(Consumer<NPC> action) {
		if (!this.isNPC())
			return;
		action.accept(this.toNPC());
	}

	/**
	 * Casts the {@link Actor} to a {@link Mob}.
	 * 
	 * @return an instance of this {@link Actor} as a {@link Mob}.
	 */
	public final NPC toNPC() {
		Preconditions.checkArgument(isNPC(), "Cannot cast this entity to npc.");
		return (NPC) this;
	}

	public void addToHitCount(Actor killer, int damage) {
		if(!killers.containsKey(killer)) {
			killers.put(killer, damage);
		} else {
			killers.put(killer, killers.get(killer) + damage);
		}
	}
	
	public Actor getKiller() {
		Actor highestHitter = null;
		int highestHit = 0;
		for(Map.Entry<Actor, Integer> entry : killers.entrySet()) {
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
	
	public void teleport(Location location) {
		this.teleportTo = location;
		if (this instanceof Player) {
			((Player) this).getWalkingQueue().reset();
		}
	}
	
	public void resetTeleportTo() {
		this.teleportTo = null;
	}
	
	public int getClientIndex() {
		if(this instanceof Player) {
			return this.getIndex() + 32768;
		} else {
			return this.getIndex();
		}
	}
	
	public boolean inCombat() {
		if (target == null && attacker == null) {
			return false;
		}
		return true;
	}
	
	public void resetCombatTurns() {
		combatTurns = 0;
	}
	
	public void incrementCombatTurns() {
		combatTurns++;
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
	
	/**
	 * Sends a delayed task for this player.
	 */
	public void task(int delay, Consumer<Actor> action) {
		Actor actor = this;
		new Task(delay, false) {
			@Override
			protected void execute() {
				action.accept(actor);
				stop();
			}
		}.submit();
	}
}
