package com.xeno;

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
	public boolean DEBUG_MODE = true;
	
	/**
	 * Represents the Networking connection states debug printing.
	 * This gets spammed pretty heavily so we're adding a specific condition.
	 */
	public boolean NETWORK_DEBUG_MODE = false;
	
	/**
	 * Represents the game servers world id.
	 */
	public int WORLD_ID = 1;
}