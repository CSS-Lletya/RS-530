package com.xeno.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an Entity within the game server.
 * @author Dennis
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Entity {
	
	/**
	 * Represents the index of the entity in the game server.
	 */
	protected transient int index;
	
	/**
	 * Represents the Location of the entity in the game server.
	 */
	protected Location location;
}