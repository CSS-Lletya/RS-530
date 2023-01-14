package com.rs;

import com.google.common.collect.ImmutableList;
import com.rs.entity.actor.item.Item;
import com.rs.world.Location;

/**
 * A generic interface containing generic data for the game server to utilize.
 * @author Dennis
 *
 */
public interface GameConstants {


	/**
	 * Represents the Game Server name.
	 */
	public final String SERVER_NAME = "#530 RS";
	
	/**
	 * Represents a state of debug print outs for developmental purposes.
	 */
	public final boolean DEBUG_MODE = true;
	
	/**
	 * Represents the Networking connection states debug printing.
	 * This gets spammed pretty heavily so we're adding a specific condition.
	 */
	public final boolean NETWORK_DEBUG_MODE = false;
	
	/**
	 * Represents the game servers world id.
	 */
	public final int WORLD_ID = 1;
	
	/**
	 * Represents the location to respawn after death, teleport, etc..
	 */
	public final Location RESPAWN_LOCATION = new Location(3222, 3222, 0);
	
	/**
	 * An immutable list of starter items given to a new player.
	 */
	public final ImmutableList<Item> STARTER = ImmutableList.of(
			new Item(995, 25), new Item(1351), new Item(590), new Item(303), new Item(315), new Item(1925), 
			new Item(1931), new Item(2309), new Item(1265), new Item(1205), new Item(1277), new Item(1171), 
			new Item(841), new Item(882, 25), new Item(556, 25), new Item(558, 12), new Item(555, 6),
			new Item(557, 4), new Item(559, 2));
}