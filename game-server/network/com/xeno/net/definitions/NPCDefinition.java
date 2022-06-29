package com.xeno.net.definitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xeno.entity.actor.npc.NPCDrop;
import com.xeno.utility.LogUtility;
import com.xeno.utility.XStreamUtil;
import com.xeno.utility.LogUtility.LogType;

/**
 * Represents a type of NPC.
 * @author Graham
 *
 */
public class NPCDefinition {

	public static final int MELEE = 0;
	public static final int RANGE = 1;
	public static final int MAGIC = 2;
	
	private static Map<Integer, NPCDefinition> definitions;

	@SuppressWarnings("unchecked")
	public static void load() throws FileNotFoundException {
		List<NPCDefinition> defs = (List<NPCDefinition>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/npcDefinitions.xml"));
		definitions = new HashMap<Integer, NPCDefinition>();
		for(NPCDefinition def : defs) {
			definitions.put(def.getId(), def);
		}
		LogUtility.log(LogType.INFO, "Loaded " + definitions.size() + " npc definitions.");
		NPCDrop.load();
	}
	
	public static NPCDefinition forId(int id) {
		NPCDefinition d = definitions.get(id);
		if(d == null) {
			d = produceDefinition(id);
		}
		return d;
	}
	
	private int id;
	private String name, examine;
	private int respawn = 20, combat = 0, hitpoints = 1, maxHit = 0, size = 1, attackSpeed = 8, attackAnim = 422, defenceAnim = 404, deathAnim = 7197;
	private boolean aggressive = false, superAggressive = false, bossMonster = false;
	private int attackType = MELEE;
	private transient NPCDrop drop = null;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExamine() {
		return examine;
	}
	
	public int getRespawn() {
		return respawn;
	}
	
	public int getCombat() {
		return combat;
	}
	
	public int getHitpoints() {
		return hitpoints;
	}
	
	public int getMaxHit() {
		return maxHit;
	}

	public int getSize() {
		return size;
	}

	public static NPCDefinition produceDefinition(int id) {
		NPCDefinition def = new NPCDefinition();
		def.id = id;
		def.name = "NPC #" + def.id;
		def.examine = "It's an NPC.";
		return def;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public int getAttackAnimation() {
		return attackAnim;
	}

	public int getDefenceAnimation() {
		return defenceAnim;
	}

	public int getDeathAnimation() {
		return deathAnim;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public int getAttackType() {
		return attackType;
	}

	public void setSuperAggressive(boolean superAggressive) {
		this.superAggressive = superAggressive;
	}

	public boolean isSuperAggressive() {
		return superAggressive;
	}

	public boolean isBoss() {
		return bossMonster;
	}

	public NPCDrop getDrop() {
		return drop;
	}

	public void setDrop(NPCDrop d) {
		this.drop = d;
	}

}
