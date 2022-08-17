package com.xeno.entity.actor;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import com.xeno.entity.Entity;
import com.xeno.entity.EntityType;
import com.xeno.entity.Follow;
import com.xeno.entity.actor.attribute.AttributeMap;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.net.entity.masks.EntityFocus;
import com.xeno.net.entity.masks.Hits;
import com.xeno.net.entity.masks.Hits.HitType;
import com.xeno.world.Location;
import com.xeno.net.entity.masks.Sprite;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents an Actor (Player/NPC) which inherits the Entity class.
 * @author Dennis
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Actor extends Entity {
	
	/**
	 * Represents a client Sprite.
	 * Not sure if this'll be here for long perhaps.
	 */
	protected transient Sprite sprite;
	
	/**
	 * Represents a future Location tile.
	 */
	protected transient Location teleportTo;
	
	/**
	 * Represents a Hit & type of hits.
	 */
	protected transient Hits hits;
	
	/**
	 * Represents an Actor set as a target.
	 */
	protected transient Actor target;
	
	/**
	 * Represents an Actor set as the attacker.
	 */
	protected transient Actor attacker;
	
	/**
	 * Represents the last Actor's previous opponent.
	 */
	protected transient Actor lastOpponent;
	
	/**
	 * Represents the time duration of the last known attack.
	 */
	protected transient long lastAttack;
	
	/**
	 * Represents the time duration of the last known attacked upon.
	 */
	protected transient long lastAttacked;
	
	/**
	 * Represents the poison amount.
	 */
	protected transient int poisonAmount;
	
	/**
	 * A collection of previous killers that attacked an Actor.
	 * Then gets determined who's the highest hitter for loot, such.
	 */
	protected transient Object2ObjectArrayMap<Actor, Integer> killers;
	
	/**
	 * Represents an instance of a Following Actor to Actor event.
	 */
	protected transient Follow follow;
	
	/**
	 * An {@link AttributeMap} instance assigned to this {@code Actor}.
	 */
	protected AttributeMap attributes = new AttributeMap();
	
	/**
	 * Represents a standard Hit to an Actor.
	 * @param damage
	 */
	public abstract void hit(int damage);
	
	/**
	 * Represents a specific Hit to an Actor.
	 * @param damage
	 */
	public abstract void hit(int damage, HitType type);
	
	/**
	 * Represents the Max Hitpoints of an Actor.
	 * @return
	 */
	public abstract int getMaxHp();
	
	/**
	 * Represents the current Hitpoints of an Actor.
	 * @return
	 */
	public abstract int getHp();
	
	/**
	 * Sets the current Hitpoints of an Actor to the specified amount.
	 * @param val
	 */
	public abstract void setHp(int val);

	
	/**
	 * Represents the Entity focus value based on the value given.
	 * @param id
	 */
	public abstract void setEntityFocus(int id);
	
	/**
	 * Represents an Actors face Location/Tile direction.
	 * @param loc
	 */
	public abstract void setFaceLocation(Location loc);
	
	/**
	 * Represents the Acots Entity focus.
	 * @return
	 */
	public abstract EntityFocus getEntityFocus();
	
	/**
	 * Represents if an Actor is auto retaliating during a combat event sequence.
	 * @return
	 */
	public abstract boolean isAutoRetaliating();
	
	/**
	 * Represents an Actors Attack animation.
	 * @return
	 */
	public abstract int getAttackAnimation();
	
	/**
	 * Represents an Actors Attack speed.
	 * @return
	 */
	public abstract int getAttackSpeed();
	
	/**
	 * Represents an Actors Hit delay.
	 * @return
	 */
	public abstract int getHitDelay();
	
	/**
	 * Represents an Actors Max Hit.
	 * @return
	 */
	public abstract int getMaxHit();
	
	/**
	 * Represents an Actors Defence Animation.
	 * @return
	 */
	public abstract int getDefenceAnimation();
	
	/**
	 * Represents an Actors Death Animation.
	 * @return
	 */
	public abstract int getDeathAnimation();
	
	/**
	 * Represents an Actors Dropped Items event on death.
	 */
	public abstract void dropLoot();
	
	/**
	 * Represents if an Actor is existent or not.
	 * @return
	 */
	public abstract boolean isValid();
	
	/**
	 * Represents the amount of Hitpoints to heal an Actor.
	 */
	public abstract void heal(int amt);
	
	public abstract void tick();

	/**
	 * Defines an Actors attributes.
	 * @param type
	 * @return actor
	 */
	public Object readResolve(EntityType type) {
		this.type = requireNonNull(type);
		hits = new Hits();
		target = null;
		attacker = null;
		poisonAmount = 0;
		killers = new Object2ObjectArrayMap<Actor, Integer>();
		sprite = new Sprite();
		attributes = new AttributeMap();
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
//		ifPlayer(p -> p.getWalkingQueue().reset());
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
	
	public boolean isPoisoned() {
		return poisonAmount > 0;
	}

	public void setPoisonAmount(int amt) {
		if (amt <= 0) {
			amt = 0;
		}
		if (this instanceof Player) {
			((Player) this).playerDetails.setPoisonAmount(amt);
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