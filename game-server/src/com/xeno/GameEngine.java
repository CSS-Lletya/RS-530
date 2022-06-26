package com.xeno;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.xeno.io.MapDataLoader;
import com.xeno.io.MapDataPacker;
import com.xeno.io.XStreamPlayerLoader;
import com.xeno.net.Server;
import com.xeno.net.WorkerThread;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.net.definitions.NPCDefinition;
import com.xeno.packethandler.PacketHandlers;
import com.xeno.util.log.Logger;
import com.xeno.world.World;

/**
 * A varek has called it before, the 'central motor' of the game.
 * 
 * That means it handles periodic updating and packet handling/creation.
 * 
 * @author Graham
 *
 */
public class GameEngine {

	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getInstance();
	
	/**
	 * Running flag.
	 */
	private boolean isRunning;
	
	/**
	 * This makes you wish that Java supported typedefs.
	 */
	private Map<Integer, int[]> mapData;
	
	/**
	 * Our worker thread.
	 */
	private WorkerThread workerThread;
	/**
	 * Thread group.
	 */
	private ThreadGroup threads = new ThreadGroup("RuneShard");
	
	/**
	 * Creates other things vital to the game logic, like the world class.
	 * @throws Exception 
	 */
	public GameEngine() throws Exception {
		/*
		 * We are running.
		 */
		isRunning = true;
		/*
		 * Check if mapdata packed file exists, if not, then we pack it.
		 */
		File packedFile = new File("data/mapdata/packed.dat");
		if(!packedFile.exists()) {
			MapDataPacker.pack("data/mapdata/unpacked/", "data/mapdata/packed.dat");
		}
		/*
		 * Actually load the mapdata.
		 */
		mapData = new HashMap<Integer, int[]>();
		MapDataLoader.load(mapData);
		/*
		 * Load handlers.
		 */
		PacketHandlers.loadHandlers();
		/*
		 * Load item definitions.
		 */
		logger.info("Loading item definitions...");
		ItemDefinition.load();
		logger.info("Loading npc definitions...");
		NPCDefinition.load();
		/*
		 * Set up the world.
		 */
		logger.info("Setting up world...");
		World.getInstance().setEngine(this);
		/*
		 * Start the worker thread.
		 */
		logger.info("Launching worker thread...");
		workerThread = new WorkerThread(new XStreamPlayerLoader());
		newThread("WorkerThread", workerThread);
	}
	
	public void newThread(String name, Runnable r) {
		new Thread(threads, r, name).start();
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
	 * Gets the is running flag.
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Sets the is running flag.
	 * @param isRunning
	 */
	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	/**
	 * Gets the worker thread.
	 * @return
	 */
	public WorkerThread getWorkerThread() {
		return workerThread;
	}
	
	/**
	 * Stops threads, saves games, etc.
	 */
	public void cleanup() {
		threads.interrupt();
	}

	public int[] getMapData(int region) {
		return mapData.get(region);
	}
	
	/**
	 * Entry point of the program.
	 * 
	 * Sets everything rolling.
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Server s = null;
				try {
					s = new Server();
				} catch (Exception e) {
					logger.error(e.toString());
					logger.stackTrace(e);
					return;
				}
				s.go();
				
			}
		}, "GameEngine").start();
	}
}
