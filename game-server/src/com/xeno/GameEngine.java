package com.xeno;

import com.xeno.net.NetworkServer;
import com.xeno.world.World;

import lombok.Getter;

/**
 * A varek has called it before, the 'central motor' of the game.
 * 
 * That means it handles periodic updating and packet handling/creation.
 * 
 * @author Graham
 * @author Dennis
 *
 */
public class GameEngine {

	/**
	 * The instance of the loader
	 */
	@Getter
	private static final GameLoader loader = new GameLoader();
	
	/**
	 * Represents an instance of the Networking server that the client connects to.
	 */
	private static NetworkServer networkServer;
	
	/**
	 * Creates other things vital to the game logic, like the world class.
	 * @throws Exception 
	 */
	public GameEngine() throws Exception {
		getLoader().getBackgroundLoader().waitForPendingTasks().shutdown();
		World.getInstance().setEngine(this);
	}
	
	/**
	 * Handle a major update.
	 */
	public void majorUpdate() {
		World.getInstance().majorUpdate();
	}
	
	/**
	 * Handle a minor update.
	 */
	public void minorUpdate() {
		World.getInstance().minorUpdate();
	}
	
	/**
	 * Called every tick.
	 */
	public void tick() {
		World.getInstance().tick();
	}
	
	/**
	 * Entry point of the program.
	 * 
	 * Sets everything rolling.
	 * @param args
	 */
	public static void main(String[] args) {
		getLoader().newThread("GameEngine", () -> {
			try {
				networkServer = new NetworkServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
			networkServer.go();
		});
	}
}