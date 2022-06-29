package com.xeno.io;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;

/**
 * Packs map data
 * 
 * @author TeleNubby
 * @author Graham
 * 
 */
public class MapDataPacker {

	/**
	 * Prevent an instance being made.
	 */
	private MapDataPacker() {
	}

	/**
	 * We actually pack the mapdata here.
	 */
	public static void pack(String from, String to) {
		LogUtility.log(LogType.INFO, "Packing mapdata...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(to));
			int i2 = 0;
			for (int i = 0; i < 16384; i++) {
				if (new File(from + i + ".txt").exists()) {
					BufferedReader in = new BufferedReader(new FileReader(from + i + ".txt"));
					for (int j = 0; j < 4; j++) {
						out.writeInt(Integer.parseInt(in.readLine()));
					}
					in.close();
					i2++;
				} else {
					for (int j = 0; j < 4; j++) {
						out.writeInt(0);
					}
				}
			}
			out.flush();
			out.close();
			LogUtility.log(LogType.INFO, "Complete.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
