package com.xeno.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xeno.entity.player.Player;

public class ObjectLocations {

	private List<WorldObject> objects;
	
	public ObjectLocations() {
		objects = new ArrayList<WorldObject>();
		loadObjects();
	}
	
	public boolean objectExists(int id, Location location) {
		for (WorldObject o : objects) {
			if (o != null) {
				if (o.getLocation().equals(location) && o.getOriginalId() == id) {
					return true;
				}
			}
		}
		return true;
	}

	public int getFace(int id, Location location) {
		for (WorldObject o : objects) {
			if (o != null) {
				if (o.getLocation().equals(location) && o.getOriginalId() == id) {
					return o.getFace();
				}
			}
		}
		return 0;
	}
	
	public void addObject(WorldObject object) {
		synchronized(objects) {
			objects.add(object);
		}
	}
	
	private void loadObjects() {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;

		try {
			characterfile = new BufferedReader(
			new FileReader("./data/objectLocations.cfg"));
		} catch (FileNotFoundException fileex) {
			return;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			return;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");

			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("o")) {
					int x = Integer.parseInt(token3[0]);
					int y = Integer.parseInt(token3[1]);
					int id = Integer.parseInt(token3[2]);
					int face = Integer.parseInt(token3[3]);
					int type = 10;
					WorldObject object = new WorldObject(id, Location.location(x, y, 0), face, type);
					objects.add(object);
				}
			} else {
				if (line.equals("[ENDOFLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {}
					return;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {}
		return;		
	}

	public boolean tileAvailable(Player p) {
		Location l = p.getLocation();
		int regionX = p.getUpdateFlags().getLastRegion().getRegionX();
		int regionY = p.getUpdateFlags().getLastRegion().getRegionY();
		int x = l.getX() - ((regionX - 6) * 8);
		int y = l.getY() - ((regionY - 6) * 8);
		return (p.getWalkingQueue().regionData[l.getZ()][x][y]) == 0;
	}
	
	public static class Region {
		
		byte height;
		byte[][] landscape;
		byte regionId;
		
		public Region(byte id) {
			this.regionId = id;
			this.landscape = new byte[104][104];
		}
	}
}
