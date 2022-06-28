package com.xeno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import com.rs.plugin.PluginManager;
import com.rs.plugin.standard.CommandPluginDispatcher;
import com.xeno.io.MapDataLoader;
import com.xeno.io.MapDataPacker;
import com.xeno.io.XStreamPlayerLoader;
import com.xeno.net.WorkerThread;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.net.definitions.NPCDefinition;
import com.xeno.packetbuilder.packets.OutgoingPacketDispatcher;
import com.xeno.util.BlockingExecutorService;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.util.TimeStamp;

import lombok.Getter;
import lombok.SneakyThrows;

public class GameLoader {

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
	 * An executor service which handles background loading tasks.
	 */
	@Getter
	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newCachedThreadPool());
	
	public GameLoader() {
		loadTasks();
	}
	
    @SneakyThrows(IOException.class)
	private void loadTasks() {
        final TimeStamp time = new TimeStamp();
		isRunning = true;
		
		File packedFile = new File("data/mapdata/packed.dat");
		if(!packedFile.exists()) {
			MapDataPacker.pack("data/mapdata/unpacked/", "data/mapdata/packed.dat");
		}
		mapData = new HashMap<Integer, int[]>();
		MapDataLoader.load(mapData);
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading item definitions...");
			try {
				ItemDefinition.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		LogUtility.log(LogType.INFO, "Loading npc definitions...");
		try {
			NPCDefinition.load();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		getBackgroundLoader().submit(() -> {
			OutgoingPacketDispatcher.load();
			CommandPluginDispatcher.load();
			PluginManager.loadPlugins();
		});

		LogUtility.log(LogType.INFO, "Launching worker thread...");
		workerThread = new WorkerThread(new XStreamPlayerLoader());
		newThread("WorkerThread", workerThread);
		LogUtility.log(LogType.INFO, "Launched game server in " + time.duration(false, "") + " milliseconds.");
	}
	
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