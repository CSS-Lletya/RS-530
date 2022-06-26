package com.xeno.io;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.xeno.util.log.Logger;

/**
 * Loads mapdata from a binary file
 * @author Graham
 *
 */
public class MapDataLoader {
	
	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getInstance();
	
	/**
	 * Prevent an instance being created.
	 */
	private MapDataLoader() {}

	/**
	 * Loads mapdata into the specified map.
	 * 
	 * The map should have a key of <code>Integer</code> and value of <code>int[]</code>.
	 * @param mapData The map.
	 * @throws IOException
	 */
	public static void load(Map<Integer, int[]> mapData) throws IOException {
		// a much simpler way than TeleNubby's
		logger.info("Reading mapdata...");
		DataInputStream in = new DataInputStream(new FileInputStream("data/mapdata/packed.dat"));
		int useableMapdata = 0;
		for(int i = 0; i < 16384; i++) {
			int[] parts = new int[4];
			for(int j = 0; j < 4; j++) {
				parts[j] = in.readInt();
			}
			if(parts[0] != 0 && parts[1] != 0 && parts[2] != 0 && parts[3] != 0) {
				useableMapdata++;
			}
			mapData.put(i, parts);
		}
		logger.info("Loaded mapdata.");
	}

}
