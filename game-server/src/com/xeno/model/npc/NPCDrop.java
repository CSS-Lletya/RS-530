package com.xeno.model.npc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xeno.model.Item;
import com.xeno.util.XStreamUtil;
import com.xeno.util.log.Logger;

/**
 * @author Graham
 *
 */
public class NPCDrop {

    public NPCDrop(){};
	
	public static Map<Integer, NPCDrop> npcDrops = new HashMap<Integer, NPCDrop>();
	@SuppressWarnings("unchecked")
	public static void load() throws FileNotFoundException {
		List<NPCDrop> list = (List<NPCDrop>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/npcDrops.xml"));
		int p =0;
		for(NPCDrop drop : list) {
			drop.ReadResolve();
			p++;
		}
		Logger.getInstance().info("Loaded " + p + " npc drops.");
	}
	
	private int[] npcs;
	private ArrayList<Item> commonDrops = null;
	private ArrayList<Item> uncommonDrops = null;
	private ArrayList<Item> rareDrops = null;
	private ArrayList<Item> alwaysDrops = null;
	
	public Object ReadResolve() {
		for (int i= 0;i< npcs.length;i++) {
			NPCDefinition.forId(npcs[i]).setDrop(this);
		}
		return this;
	}
	
	public ArrayList<Item> getCommonDrops() {
		return commonDrops;
	}
	
	public ArrayList<Item> getUncommonDrops() {
		return uncommonDrops;
	}
	
	public ArrayList<Item> getRareDrops() {
		return rareDrops;
	}
	
	public ArrayList<Item> getAlwaysDrops() {
		return alwaysDrops;
	}
}