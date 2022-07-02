package com.xeno.entity.actor.attribute;

/**
 * A Type-safe declaration interface to simplify attribute declaring.
 * @author Dennis
 *
 */
public interface Attribute {

	/**
	 * Represents a Locked state where an Actor is unable to move and or operate.
	 */
	public String LOCKED = "LOCKED";
	
	/**
	 * Represents the last window state.
	 */
	public String LAST_WINDOW_TYPE = "lastWindowType";
	
	/**
	 * Represents the state of an item being withdrawn from a Players bank as either a Noted version of basic Item.
	 */
	public String WITHDRAW_ITEM_AS_NOTE = "withdrawNote";
	
	/**
	 * Represents a login state check.
	 */
	public String SEND_LOGIN = "sendLogin";
	
	/**
	 * Represents the status of a Player to check if they're in the Wilderness
	 */
	public String IN_WILDERNESS = "inWild";
	
	/**
	 * Represents the status of a Player to check if they're in a Multi Zone (ex: multiple npcs vs player)
	 */
	public String IN_MULTI_ZONE = "inMulti";
	
	/**
	 * Represents a state check for hair modifications to a Player during the designing event.
	 */
	public String HAIR_TOGGLE = "hairToggle";
	
	/**
	 * Represents the Home teleporting state check.
	 */
	public String HOME_TELEPORTING = "homeTeleporting";
	
	/**
	 * Represents the teleporting state check.
	 */
	public String TELEPORTING = "teleporting";
	
	/**
	 * Represents the Skill Menu state check.
	 */
	public String SKILL_MENU = "skillMenu";
	
	/**
	 * Represents a state of an Actor being Frozen from combat, not to be confused for the {@link #LOCKED}.
	 */
	public String FROZEN = "frozen";
	
	/**
	 * Represents a state where an Actor is hidden from the active game server and from specific events.
	 */
	public String HIDDEN = "hidden";
	
	/**
	 * Represents if an Actor is dead.
	 */
	public String DEAD = "dead";
}