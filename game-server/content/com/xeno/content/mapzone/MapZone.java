package com.xeno.content.mapzone;

import com.xeno.entity.Location;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.item.GroundItem;
import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.world.WorldObject;

import io.vavr.collection.Array;
import lombok.Data;

/**
 * Represents a Map zone (Controller if it sounds more familiar) where events or
 * states take place.
 * 
 * @author Dennis
 *
 */
@Data
public abstract class MapZone {

	/**
	 * The string which defines the current map zone name.
	 */
	private final String mapeZoneName;

	/**
	 * The current name of this map zone.
	 */
	private final MapZoneSafetyCondition safety;

	/**
	 * The current type of this map zone.
	 */
	private final MapZoneType type;

	/**
	 * Represents a collection of restrictions a map zone may contain.
	 */
	private final ZoneRestriction[] restrictions;

	/**
	 * If restriction flag.
	 */
	private int restriction;

	/**
	 * Constructs a new {@code MapZone} {@code Object}.
	 * 
	 * @param name         The name.
	 * @param members      If the map zone is members only.
	 * @param overlappable If the zone can be overlapped.
	 */
	public MapZone(String mapeZoneName, MapZoneSafetyCondition safety, MapZoneType type,
			ZoneRestriction... restrictions) {
		this.mapeZoneName = mapeZoneName;
		this.safety = safety;
		this.type = type;
		this.restrictions = restrictions;
		Array.of(restrictions).forEach(this::addRestriction);
	}

	/**
	 * Represents the beginning of a Map Zone event.
	 * 
	 * @param player
	 */
	public abstract void start(Player player);

	/**
	 * Determines if {@code player} is in this map zone.
	 * most of the time it'll be true unless specific boundaries are required
	 * 
	 * @param player the player to determine this for.
	 * @return <true> if this map zone contains the player, <false> otherwise.
	 */
	public boolean contains(Player player) {
		return true;
	}

	/**
	 * Represents the finishing event if present for ending a Map Zone event. Used
	 * mainly for retrieving Items, such from a player when leaving.
	 * 
	 * @param player
	 */
	public abstract void finish(Player player);

//	public boolean canEat(Player player, Food food) {
//		return true;
//	}
//
//	public boolean canPot(Player player, Pot pot) {
//		return true;
//	}

	/**
	 * Represents a state if a Player can take an Item from the ground.
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean canTakeItem(Player player, GroundItem item) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you attacking
	 */
	public boolean keepCombating(Player player, Actor target) {
		return true;
	}

	/**
	 * Represents a state which a player can or cannot equip an Item.
	 * 
	 * @param player
	 * @param slotId
	 * @param itemId
	 * @return
	 */
	public boolean canEquip(Player player, int slotId, int itemId) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you start trying to
	 * attack
	 */
	public boolean canAttack(Player player, Actor target) {
		return true;
	}

	public boolean canUseItemOnItem(Player player, Item itemUsed, Item usedWith) {
		return true;
	}

	public boolean canPlayerOption1(Player player, Player target) {
		return true;
	}

	public boolean canPlayerOption2(Player player, Player target) {
		return true;
	}

	public boolean canPlayerOption3(Player target) {
		return true;
	}

	public boolean canPlayerOption4(Player target) {
		return true;
	}

	/**
	 * hits as ice barrage and that on multi areas
	 */
	public boolean canHit(Player player, Actor entity) {
		return true;
	}

	/**
	 * processes every game tick, usually not used
	 */
	public void process(Player player) {

	}

	public void moved(Player player) {

	}

	/**
	 * called once teleport is performed
	 */
	public void magicTeleported(Player player, int type) {

	}

	/**
	 * return can teleport
	 */
	public boolean processMagicTeleport(Player player, Location toTile) {// TODO: add this when finishing new system
		return true;
	}

	/**
	 * event not created yet. return can teleport
	 */
	public boolean processItemTeleport(Player player, Location toTile) {
		return true;
	}

	/**
	 * this is used for things like Levers, such. return can teleport
	 */
	public boolean processObjectTeleport(Player player, Location toTile) {
		return true;
	}

	/**
	 * return process normally
	 */
	public boolean processObjectClick1(Player player, WorldObject object) {
		return true;
	}

	/**
	 * return process normally
	 * 
	 * @param slotId2 TODO
	 */
	public boolean processButtonClick(Player player, int interfaceId, int componentId, int slotId) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick1(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick2(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick3(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick4(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick2(Player player, WorldObject object) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick3(Player player, WorldObject object) {
		return true;
	}

	public boolean processObjectClick4(Player player, WorldObject object) {
		return true;
	}

	public boolean processObjectClick5(Player player, WorldObject object) {
		return true;
	}

	public boolean handleItemOnObject(Player player, WorldObject object, Item item) {
		return true;
	}

	/**
	 * return let default death
	 */
	public boolean sendDeath(Player player) {// not event made yet
		return true;
	}

	public void processNPCDeath(Player player, int id) {// no event made yet

	}

	/**
	 * return can move that step
	 */
	public boolean canMove(Player player, int dir) {
		return true;
	}

	/**
	 * return remove map zone
	 */
	public void login(Player player) {
	}

	/**
	 * return remove map zone
	 */
	public void logout(Player player) {
	}

	public boolean processItemOnNPC(Player player, NPC npc, Item item) {
		return true;
	}

	public boolean canDropItem(Player player, Item item) {
		return true;
	}

	public boolean canSummonFamiliar(Player player) {// TODO for summoning
		return true;
	}

	public boolean processItemOnPlayer(Player player, Player target, Item item) {// missing packet
		return true;
	}

	/**
	 * The enumerated type whose elements represent the map zone types.
	 * 
	 * @author lare96 <http://github.com/lare96>
	 */
	public enum MapZoneType {
		NORMAL, TIMED
	}

	/**
	 * The enumerated type whose elements represent the item safety of a player who
	 * is playing the map zone.
	 * 
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum MapZoneSafetyCondition {
		/**
		 * This safety is similar to when a player dies while he is skulled.
		 */
		DANGEROUS,
		/**
		 * /** Indicates the map zone is fully safe and no items will be lost on death
		 */
		SAFE
	}

	/**
	 * Adds a restriction flag.
	 * 
	 * @param restriction The restriction flag.
	 */
	public void addRestriction(ZoneRestriction restriction) {
		addRestriction(restriction.getFlag());
	}

	/**
	 * Adds a restriction flag.
	 * 
	 * @param flag The restriction flag.
	 */
	public void addRestriction(int flag) {
		restriction |= flag;
	}

	/**
	 * Checks if the restriction was flagged.
	 * 
	 * @param flag The restriction flag.
	 * @return {@code True} if so.
	 */
	public boolean isRestricted(int flag) {
		return (restriction & flag) != 0;
	}
	
	/**
	 * Checks if the restriction was flagged.
	 * 
	 * @param flag The restriction flag.
	 * @return {@code True} if so.
	 */
	public boolean isRestricted(ZoneRestriction zone) {
		return (restriction & zone.getFlag()) != 0;
	}

	/**
	 * Gets the restriction.
	 * 
	 * @return The restriction.
	 */
	public int getRestriction() {
		return restriction;
	}
}