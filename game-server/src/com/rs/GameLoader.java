package com.rs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import com.rs.entity.actor.attribute.AttributeKey;
import com.rs.io.MapDataLoader;
import com.rs.io.MapDataPacker;
import com.rs.io.XStreamPlayerLoader;
import com.rs.net.WorkerThread;
import com.rs.net.definitions.ItemDefinition;
import com.rs.net.definitions.NPCDefinition;
import com.rs.net.definitions.ObjectDefinitions;
import com.rs.packetbuilder.packets.OutgoingPacketDispatcher;
import com.rs.plugin.PluginManager;
import com.rs.plugin.standard.CommandPluginDispatcher;
import com.rs.plugin.standard.RSInterfacePluginDispatcher;
import com.rs.utility.BlockingExecutorService;
import com.rs.utility.LogUtility;
import com.rs.utility.LogUtility.LogType;
import com.rs.utility.TimeStamp;
import com.rs.world.Region;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Represents a class that loads all core startup
 * prerequisites for the game server to utilize.
 * @author Dennis
 *
 */
public class GameLoader {

	/**
	 * Running flag.
	 */
	private boolean isRunning;
	
	/**
	 * This makes you wish that Java supported typedefs.
	 */
	private Object2ObjectArrayMap<Integer, int[]> mapData;
	
	/**
	 * Our worker thread.
	 */
	private WorkerThread workerThread;
	
	/**
	 * Thread group.
	 */
	private ThreadGroup threads = new ThreadGroup("RuneShard");
	
	/**
	 * An executor service which handles background loading tasks.
	 */
	@Getter
	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newCachedThreadPool());
	
	/**
	 * Constructs the Game Loader class and loads the tasks inside.
	 */
	public GameLoader() {
		loadTasks();
	}
	
	/**
	 * Loads the game server prerequisites and important data.
	 */
    @SneakyThrows(IOException.class)
	private void loadTasks() {
		isRunning = true;
		File packedFile = new File("data/mapdata/packed.dat");
		if(!packedFile.exists()) {
			MapDataPacker.pack("data/mapdata/unpacked/", "data/mapdata/packed.dat");
		}
		mapData = new Object2ObjectArrayMap<Integer, int[]>();
		MapDataLoader.load(mapData);
		Region.load();
		getBackgroundLoader().submit(() -> {
			try {
				LogUtility.log(LogType.INFO, "Loading item definitions...");
				ItemDefinition.load();
				LogUtility.log(LogType.INFO, "Loading object definitions... - NOT FINISHED");
				ObjectDefinitions.loadConfig();
				LogUtility.log(LogType.INFO, "Loading npc definitions...");
				NPCDefinition.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		getBackgroundLoader().submit(() -> {
			OutgoingPacketDispatcher.load();
			CommandPluginDispatcher.load();
			RSInterfacePluginDispatcher.load();
			AttributeKey.init();
		});
		PluginManager.loadPlugins();
		LogUtility.log(LogType.INFO, "Launching worker thread...");
		workerThread = new WorkerThread(new XStreamPlayerLoader());
		newThread("WorkerThread", workerThread);
	}
	
    /**
     * Creates a new thread (simplified)
     * @param name
     * @param r
     */
	public void newThread(String name, Runnable r) {
		new Thread(threads, r, name).start();
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
}
