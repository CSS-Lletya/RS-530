package com.rs.entity;

import com.rs.world.Location;

import lombok.Data;

/**
 * Represents an Entity within the game server.
 * @author Dennis
 *
 */
@Data
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