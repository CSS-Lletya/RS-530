package com.rs.entity.actor.item;

import java.util.Map;

import com.rs.net.definitions.ItemDefinition;

public final class ItemConstants {
	
	public static int getNotedItem(int item) {
		ItemDefinition itemDef = ItemDefinition.forId(item);
		ItemDefinition nextItem = ItemDefinition.forId(item + 1);
		if (nextItem.getName().equalsIgnoreCase(itemDef.getName())) {
			if (nextItem.isStackable() && nextItem.isNoted()) {
				return nextItem.getId();
			}
		}
		for (Map.Entry<Integer, ItemDefinition> entry : ItemDefinition.getDefinitions().entrySet()) {
			if (entry.getValue().getName().equals(itemDef.getName())) {
				if (entry.getValue().getExamine().startsWith("Swap") && !entry.getValue().equals(itemDef)) {
					return entry.getValue().getId();
				}
			}
		}
		return itemDef.getId();
	}
	
	public static int getUnNotedItem(int item) {
		ItemDefinition itemDef = ItemDefinition.forId(item);
		ItemDefinition previousItem = ItemDefinition.forId(item == 0 ? item  : item - 1);
		if (previousItem.getName().equalsIgnoreCase(itemDef.getName())) {
			if (!previousItem.isStackable() && !previousItem.isNoted()) {
				return previousItem.getId();
			}
		}
		for (Map.Entry<Integer, ItemDefinition> entry : ItemDefinition.getDefinitions().entrySet()) {
			if (entry.getValue().getName().equals(itemDef.getName()) && !entry.getValue().equals(itemDef)) {
				if (!entry.getValue().getExamine().startsWith("Swap")) {
					return entry.getValue().getId();
				}
			}
		}
		return itemDef.getId();
	}
	
	public static boolean isPlayerBound(int item) {
		for (int i = 0; i < PLAYER_BOUND_ITEMS.length; i++) {
			if (item == PLAYER_BOUND_ITEMS[i]) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Unused
	 */
	public static boolean isNote(int item) {
		ItemDefinition itemDef = ItemDefinition.forId(item);
		ItemDefinition previousItem = ItemDefinition.forId(item == 0 ? item  : item - 1);
		if (!itemDef.isStackable()) {
			return false;
		}
		if (itemDef.getExamine().startsWith("Swap")) {
			return true;
		}
		if (previousItem.getName().equalsIgnoreCase(itemDef.getName())) {
			if (!previousItem.isStackable() && itemDef.isStackable()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFullBody(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_BODY.length; i++) {
			if (weapon.contains(FULL_BODY[i]) || def.getId() == 544) {
				return true;
			}
		}
		return false;
	}

 
	public static boolean isFullHat(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (weapon.endsWith(FULL_HAT[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_MASK.length; i++) {
			if (weapon.endsWith(FULL_MASK[i])) {
				return true;
			}
		}
		return false;
	}
	
	private static final String[] CAPES = {"cape","Cape","cloak", "ava"};
	private static final String[] HATS = {"Bunny","sallet","cowl", "helm","hood","tiara","coif","Coif","hat","partyhat","Hat","full helm (t)","full helm (g)","hat (t)","hat (g)","cav","boater","helmet","mask","Helm of neitiznot"};
	private static final String[] BOOTS = {"boots","Boots"};
	private static final String[] GLOVES = {"gloves","gauntlets","Gloves","vambraces","vamb","bracers"};
	private static final String[] SHIELDS = {"Toktz-ket-xil","kiteshield","sq shield","Toktz-ket","books","book","kiteshield (t)","kiteshield (g)","kiteshield(h)","defender","shield"};
	private static final String[] AMULETS = {"scarf","stole", "amulet","necklace","Amulet of"};
	private static final String[] ARROWS = {"arrow","arrows","bolts (e)","bolt (e)","arrow(p)","arrow(+)","arrow(s)","bolt","bolts","Bolt rack","Opal bolts","Dragon bolts"};
	private static final String[] RINGS = {"ring", "Ring"};
	private static final String[] BODY = {"hauberk","platebody","chainbody","robetop","leathertop","platemail","top","brassard","Robe top","body","platebody (t)","platebody (g)","body(g)","body_(g)","chestplate","torso","shirt","Runecrafter robe",};
	private static final String[] LEGS = {"cuisse","knight robe", "platelegs","plateskirt","skirt","bottoms","chaps","platelegs (t)","platelegs (g)","bottom","skirt","skirt (g)","skirt (t)","chaps (g)","chaps (t)","tassets","legs"};
	private static final String[] WEAPONS = {"sceptre", "Tzhaar-Ket-Om","Excalibur","dark bow", "Pharaoh's","wand", "adze", "Karil's x-bow","warhammer","claws","scimitar","longsword","sword","longbow","shortbow","dagger","mace","halberd","spear",
	"whip","axe","flail","crossbow","Torags hammer's","dagger(p)", "dagger(p+)","dagger(p++)","dagger(+)","dagger(s)","spear(p)","spear(+)",
	"spear(s)","spear(kp)","maul","dart","dart(p)","javelin","javelin(p)","knife","knife(p)","Longbow","Shortbow",
	"Crossbow","Toktz-xil","Toktz-mej","Tzhaar-ket","staff","Staff","godsword","c'bow","Crystal bow","Dark bow", "anchor"};
	/* Fullbody is an item that covers your arms. */
	private static final String[] FULL_BODY = {"Morrigan's leather body", "hauberk","Ghostly robe","Monk's robe","Granite","Vesta", "Runecrafter robe", "top","shirt","platebody","Ahrims robetop","Karils leathertop","brassard","Robe top","robetop","platebody (t)","platebody (g)","chestplate","torso", "Dragon chainbody"};
	/* Fullhat covers your head but not your beard. */
	private static final String[] FULL_HAT = {"3rd age","cowl", "Berserker","med helm","coif","Dharoks helm","hood","Initiate helm","Coif","Helm of neitiznot"};
	/* Fullmask covers your entire head. */
	private static final String[] FULL_MASK = {"sallet","full helm(t)", "full helm(g)","full helm","mask","Verac's helm","Guthan's helm","Torag's helm","Karil's coif","full helm (t)","full helm (g)","mask"};

	public static int getItemType(int wearId) {
		String weapon = ItemDefinition.forId(wearId).getName().toLowerCase();
		for(int i = 0; i < CAPES.length; i++) {
			if(weapon.contains(CAPES[i]))
				return 1;
		}
		for(int i = 0; i < HATS.length; i++) {
			if(weapon.contains(HATS[i]))
				return 0;
		}
		for(int i = 0; i < BOOTS.length; i++) {
			if(weapon.endsWith(BOOTS[i]) || weapon.startsWith(BOOTS[i]))
				return 10;
		}
		for(int i = 0; i < GLOVES.length; i++) {
			if(weapon.endsWith(GLOVES[i]) || weapon.startsWith(GLOVES[i]))
				return 9;
		}
		for(int i = 0; i < SHIELDS.length; i++) {
			if(weapon.contains(SHIELDS[i]))
				return 5;
		}
		for(int i = 0; i < AMULETS.length; i++) {
			if(weapon.endsWith(AMULETS[i]) || weapon.startsWith(AMULETS[i]))
				return 2;
		}
		for(int i = 0; i < ARROWS.length; i++) {
			if(weapon.endsWith(ARROWS[i]) || weapon.startsWith(ARROWS[i]))
				return 13;
		}
		for(int i = 0; i < RINGS.length; i++) {
			if(weapon.endsWith(RINGS[i]) || weapon.startsWith(RINGS[i]))
				return 12;
		}
		for(int i = 0; i < BODY.length; i++) {
			if(weapon.contains(BODY[i]) || wearId == 544 || wearId == 6107 || wearId == 1037)
				return 4;
		}
		for(int i = 0; i < LEGS.length; i++) {
			if(weapon.contains(LEGS[i]) || wearId == 542 || wearId == 6108 || wearId == 1033)
				return 7;
		}
		for(int i = 0; i < WEAPONS.length; i++) {
			if(weapon.endsWith(WEAPONS[i]) || weapon.startsWith(WEAPONS[i]))
				return 3;
		}
		return -1;
	}
	
	public static boolean isTwoHanded(int itemID) {
		String weapon = ItemDefinition.forId(itemID).getName();
		for (int i = 0; i < TWO_HANDED.length; i++) {
			if (weapon.endsWith(TWO_HANDED[i]) || weapon.startsWith(TWO_HANDED[i])) {
				return true;
			}
		}
		return false;
	}
	
	private static final String[] TWO_HANDED = {"shortbow", "longbow", "godsword", "greataxe", "flail", "halberd", "hammer", "spear", "hammers", "2h sword", 
		"Saradomin sword", "Granite maul", "claws", "Karil's crossbow", "Tzhaar-ket-om", "crystal bow", "Dark bow"};
	
	/**
	 * An array of non-tradable items.
	 * First block of items don't get destroyed when dropped, the second block do.
	 */
	public static final int[] PLAYER_BOUND_ITEMS = {
		6570, // Fire cape.
		2996, // Agility ticket.
		10551, // Fighter torso.
		8850, // Rune defender.
		8840, // Void skirt.
		8839, // Void top.
		11665, // Melee void helm.
		11663, // Range void helm.
		11664, // Mage void helm.
		8842, // Void gloves.
		8851, // Warrior guild token.
	};
}
