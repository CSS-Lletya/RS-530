package com.xeno.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import com.xeno.util.ItemData;
import com.xeno.util.log.Logger;

/**
 * Item Definition class
 * @author Graham
 * @author Luke132
 *
 */
public class ItemDefinition {
	
	private static Map<Integer, ItemDefinition> definitions = null;
	@SuppressWarnings("unchecked")
	/*public static void load() throws IOException {
		definitions = (Map<Integer, ItemDefinition>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/itemDefinitions.xml"));

		final int[] untradable = {6570};
		for (int i = 0; i < untradable.length; i++) {
			forId(untradable[i]).setPlayerBound(true);
		}
		Logger.getInstance().info("Loaded " + getDefinitions().size() + " item definitions.");
	}*/
	
	 /* public static void write()throws IOException{
	    BufferedWriter output = null;
	    File file = new File("data/itemList.cfg");
	    output = new BufferedWriter(new FileWriter(file));
	    for(Map.Entry<Integer, ItemDefinition> i : definitions.entrySet()) {
	    	ItemDefinition id = i.getValue();
	    	output.write("item = " + id.getId());
	    	output.write("\t");
	    	output.write(id.getName());
	    	output.write("\t");
	    	output.write(id.getExamine());
	    	output.write("\t");
	    	output.write(""+id.getEquipId());
	    	output.write("\t");
	    	output.write(id.isNoted() ? "1" : "0");
	    	output.write("\t");
	    	output.write(id.isStackable() ? "1" : "0");
	    	output.write("\t");
	    	output.write(""+id.getPrice().getMinimumPrice());
	    	output.write("\t");
	    	output.write(""+id.getPrice().getNormalPrice());
	    	output.write("\t");
	    	output.write(""+id.getPrice().getMaximumPrice());
	    	output.write("\t");
			for(int j = 0; j < 13; j++) {
				output.write(""+id.getBonus(j)+"\t");
			}
			output.newLine();
	    }
	    output.write("[ENDOFLIST]");
	    output.close();
	    System.out.println("Your file has been written");        
    }*/
	
	public static void load() throws IOException {
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
			new FileReader("./data/itemList.cfg"));
		} catch (FileNotFoundException fileex) {
			return;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			return;
		}
		definitions = new HashMap<Integer, ItemDefinition>();
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");

			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("item")) {
					ItemDefinition def = new ItemDefinition();
					def.id = Integer.parseInt(token3[0]);
					def.name = token3[1];
					def.examine = token3[2];
					def.equipId = Integer.parseInt(token3[3]);
					int noted = Integer.parseInt(token3[4]);
					def.noted = noted == 1;
					int stackable = Integer.parseInt(token3[5]);
					def.stackable = stackable == 1;
					def.price = new ItemPrice();
					def.price.normPrice = Integer.parseInt(token3[6]);
					def.price.minPrice = Integer.parseInt(token3[7]);
					def.price.maxPrice = Integer.parseInt(token3[8]);
					def.bonus = new int[13];
					int i = 9;
					for(int j = 0; j < 13; j++) {
						def.bonus[j] = Integer.parseInt(token3[i]);
						i++;
					}
					definitions.put(def.getId(), def);
				}
			} else {
				if (line.equals("[ENDOFLIST]")) {
					try {
						characterfile.close();
						for (int i = 0; i < ItemData.PLAYER_BOUND_ITEMS.length; i++) {
							forId(ItemData.PLAYER_BOUND_ITEMS[i]).setPlayerBound(true);
						}
						Logger.getInstance().info("Loaded " + definitions.size() + " item definitions.");
						setNotedItemPrices();
						loadEquipIds();
						loadRenderIds();
						loadExamines();
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
	
	private static void loadExamines() throws IOException {
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
			new FileReader("./lists/itemExamines.txt"));
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
			int spot = line.indexOf("");

			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				int i = line.indexOf(":");
				int item = Integer.parseInt(line.substring(0, i));
				if (item >= 629) {
					item--; // mike messed up dumping.
				}
				if (item < 14629) {
					String examine = line.substring(i+1);
					ItemDefinition def = definitions.get(item);
					def.setExamine(examine);
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

	public static void loadEquipIds() {
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
			new FileReader("./data/equips.txt"));
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
			int spot = line.indexOf(":");

			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("ItemId")) {
					int item = Integer.parseInt(token3[0]);
					int equipId = Integer.parseInt(token3[1]);
					if (item <= 14630) {
						ItemDefinition def = forId(item);
						def.setEquipId(equipId);
					}
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

	public static void loadRenderIds() throws IOException {
		File f = new File("./data/render.txt");        
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        
        StringBuffer sb = new StringBuffer();
        String eachLine = br.readLine();
        int itemId = 0;
        while(!eachLine.equalsIgnoreCase("[ENDOFLIST]")) {
      
	            if (eachLine.contains("itemId:")) {
	            	itemId = Integer.parseInt(eachLine.substring(8));
	            }
	            if (eachLine.contains("renderEmote")) {
	            	if (itemId <= 14630) {
		            	ItemDefinition def = forId(itemId);
		            	int anim = Integer.parseInt(eachLine.substring(12));
		            	def.setAnimation(anim);
	            	}
	            }
            eachLine = br.readLine();
        }
        br.close();
	}

	public static void setNotedItemPrices() {
		for(Map.Entry<Integer, ItemDefinition> i : definitions.entrySet()) {
			ItemDefinition def = i.getValue();
			if (def.isNoted()) {
				int unNoted = ItemData.getUnNotedItem(def.getId());
				if (unNoted != -1) {
					def.price = forId(unNoted).getPrice();
				}
			}
		}
	}
	
	//TODO rewrite all lists with noted/unnoted prices the same.
	/*public static void load() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("data/itemDefinitions.dat", "r");
		ByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
		int amt = buffer.getInt();
		definitions = new HashMap<Integer, ItemDefinition>();
		for(int i = 0; i < amt; i++) {
			ItemDefinition def = new ItemDefinition();
			def.id = buffer.getShort();
			def.equipId = buffer.getShort();
			def.noted = buffer.get() == 1;
			def.stackable = buffer.get() == 1;
			def.price = new ItemPrice();
			def.price.normPrice = buffer.getInt();
			def.price.minPrice = buffer.getInt();
			def.price.maxPrice = buffer.getInt();
			def.bonus = new int[13];
			for(int j = 0; j < 13; j++) {
				def.bonus[j] = buffer.get();
			}
			StringBuilder s = new StringBuilder();
			byte b = 0;
			while((b = buffer.get()) != 0) {
				s.append((char) b);
			}
			def.name = s.toString();
			s = new StringBuilder();
			while((b = buffer.get()) != 0) {
				s.append((char) b);
			}
			def.examine = s.toString();
			definitions.put(def.getId(), def);
		}
		final int[] untradable = {6570};
		for (int i = 0; i < untradable.length; i++) {
			forId(untradable[i]).setPlayerBound(true);
		}
		Logger.getInstance().info("Loaded " + definitions.size() + " item definitions.");
		loadPrices();
		setNotedItemPrices();
		//write();
	}*/
	
	/*
	 * I use a CFG file since it is easier to edit 
	 * not really i'm just a noob :(.
	 */
	public static boolean loadPrices() {
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
			new FileReader("./data/itemPrices.cfg"));
		} catch (FileNotFoundException fileex) {
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			return false;
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
				if (token.equals("item")) {
					int item = Integer.parseInt(token3[0]);
					int min = Integer.parseInt(token3[1]);
					int max = Integer.parseInt(token3[2]);
					ItemDefinition lol = definitions.get(item);
					lol.getPrice().minPrice = min;
					lol.getPrice().maxPrice = max;
				}
			} else {
				if (line.equals("[ENDOFITEMLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {}
					return true;
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
		return false;
	}

	public static ItemDefinition forId(int id) {
		ItemDefinition i = definitions.get(id);
		return i;
	}
	
	public static class ItemPrice {
		private int minPrice = 1;
		private int maxPrice = 3;
		private int normPrice = 2;
		
		public int getMinimumPrice() {
			return minPrice;
		}
		
		public int getMaximumPrice() {
			return maxPrice;
		}
		
		public int getNormalPrice() {
			return (maxPrice + minPrice) / 2;
		}
	}
	
	private ItemPrice price;
	private int[] bonus;
	private String examine;
	private int id;
	private boolean stackable;
	private String name;
	private boolean noted;
	private int equipId;
	private transient boolean playerBound;
	private int animation = 1426;
	
	private void setEquipId(int id) {
		equipId = id;
	}
	
	public ItemPrice getPrice() {
		return price;
	}
	
	public int getEquipId() {
		return equipId;
	}
	
	public int[] getBonuses() {
		return bonus;
	}

	public int getBonus(int id) {
		return bonus[id];
	}

	public String getExamine() {
		return examine;
	}
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isNoted() {
		return noted;
	}

	public boolean isStackable() {
		return stackable;
	}

	public static Map<Integer, ItemDefinition> getDefinitions() {
		return definitions;
	}
	
	public void setPlayerBound(boolean playerBound) {
		this.playerBound = playerBound;
	}
	
	public boolean isPlayerBound() {
		return playerBound;
	}

	public void setNoted(boolean noted) {
		this.noted = noted;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public int getAnimation() {
		return animation;
	}
	
	public void setExamine(String e) {
		this.examine = e;
	}

}
