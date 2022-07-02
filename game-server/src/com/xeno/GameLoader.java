package com.xeno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;

import com.rs.plugin.PluginManager;
import com.rs.plugin.standard.CommandPluginDispatcher;
import com.rs.plugin.standard.RSInterfacePluginDispatcher;
import com.xeno.entity.actor.attribute.AttributeKey;
import com.xeno.io.MapDataLoader;
import com.xeno.io.MapDataPacker;
import com.xeno.io.XStreamPlayerLoader;
import com.xeno.net.WorkerThread;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.net.definitions.NPCDefinition;
import com.xeno.net.definitions.ObjectDefinitions;
import com.xeno.packetbuilder.packets.OutgoingPacketDispatcher;
import com.xeno.utility.BlockingExecutorService;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.utility.TimeStamp;
import com.xeno.world.Region;

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
        final TimeStamp time = new TimeStamp();
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
				LogUtility.log(LogType.INFO, "Loading object definitions...");
				ObjectDefinitions.loadConfig();
				LogUtility.log(LogType.INFO, "Loading npc definitions...");
				NPCDefinition.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		getBackgroundLoader().submit(() -> {
			AttributeKey.init();
			
		});
		getBackgroundLoader().submit(() -> {
			OutgoingPacketDispatcher.load();
			CommandPluginDispatcher.load();
			RSInterfacePluginDispatcher.load();
			PluginManager.loadPlugins();
		});

		LogUtility.log(LogType.INFO, "Launching worker thread...");
		workerThread = new WorkerThread(new XStreamPlayerLoader());
		newThread("WorkerThread", workerThread);
		LogUtility.log(LogType.INFO, "Launched game server in " + time.duration(false, "") + " milliseconds.");
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
