package com.xeno.content.combat.constants;

import com.xeno.entity.npc.NPC;
import com.xeno.entity.player.Player;
import com.xeno.net.definitions.ItemDefinition;

public class Animations {

	public Animations() {
		
	}
	
	public static int getAttackAnimation(Player p) {
		String weapon = p.getEquipment().getSlot(3).getDefinition().getName();
		if (weapon.contains("whip")) {
			return 1658;
		}
		if (weapon.contains("2h") || weapon.contains("godsword") || weapon.contains("Saradomin sword")) {
			return 7041;
		}
		if (weapon.contains("shortbow") || weapon.contains("longbow") || weapon.contains("Crystal bow") || weapon.contains("Dark bow")) {
			return 426;
		}
		if (weapon.endsWith("crossbow") && !weapon.contains("Karil's")) {
			return 4230;
		}
		if (weapon.endsWith("xil-ul")) {
			return 2614;
		}
		if (weapon.contains("Karil")) {
			return 2075;
		}
		if (weapon.contains("claws")) {
			return 451;
		}
		if (weapon.contains("halberd")) {
			return 440;
		}
		if (weapon.contains("battleaxe")) {
			return 395;
		}
		if (weapon.contains("pickaxe") || weapon.contains("mace") || weapon.contains("warhammer") || weapon.contains("staff") || weapon.contains("Staff") || weapon.contains("wand")) {
			return 393;
		}
		if (weapon.contains("Granite maul")) {
			return 1665;
		}
		if (weapon.contains("dagger")) {
			return 376;
		}
		if (weapon.contains("Dharok")) {
			return 2067;
		}
		if (weapon.contains("hammer")) {
			return 2068;
		}
		if (p.getEquipment().getItemInSlot(3) <= 0) {
			return 422;
		}
		if (weapon.contains("flail")) {
			return 2062;
		}
		if (weapon.contains("Guthan") || weapon.contains("spear")) {
			return 2080;
		}
		if (weapon.contains("thrownaxe")) {
			return 385;
		}
		if (weapon.contains("mace")) {
			return 393;
		}
		if (!weapon.contains("Dragon") && weapon.contains("longsword") || weapon.endsWith(" sword")) {
			return 400;
		}
		if (weapon.contains("Dragon longsword") || weapon.contains("scimitar")) {
			return 451;
		}
		return 422;
	}

	public static int getAttackSpeed(Player p) {
		String weapon = p.getEquipment().getSlot(3).getDefinition().getName();
		if (weapon.contains("dart") || weapon.contains("knife")) {
			return 3;
		}
		if (weapon.contains("whip") || weapon.contains("dagger") || weapon.endsWith(" sword") || weapon.contains("scimitar") || weapon.contains("claws")
				|| weapon.contains("Toktz-xil-ak") || weapon.contains("Toktz-xil-ek") || weapon.contains("Saradomin sword") || weapon.contains("Saradomin staff") 
				|| weapon.contains("Guthix staff") || weapon.contains("Zamorak staff") || weapon.contains("Slayer") || weapon.contains("ancient") || weapon.contains("shortbow")
				|| weapon.contains("Karil") || weapon.contains("Toktz-xil-ul")) {
			return 4;
		}
		if (weapon.contains("longsword") || weapon.contains("mace") || weapon.endsWith(" axe") || (weapon.contains("spear") && !weapon.contains("Guthan")) || weapon.contains("pickaxe")
				|| weapon.contains("Tzhaar-ket-em") || weapon.contains("hammer") || weapon.contains("flail") 
				|| (weapon.contains("staff") && !weapon.contains("Guthix") && !weapon.contains("Saradomin") && ! weapon.contains("Zamorak") && ! weapon.contains("Slayer")) || weapon.contains("Staff")
				|| weapon.contains("Iban") || weapon.contains("composite") || weapon.contains("Seercull") || weapon.contains("Crystal") || weapon.contains("thrownaxe")) {
			return 5;
		}
		if (weapon.endsWith("battleaxe") || weapon.contains("warhammer") || weapon.contains("godsword") || weapon.contains("Toktz-mej-tal") || weapon.contains("Ahrim")
				|| weapon.contains("Zuriel") || weapon.contains("longbow") || (weapon.endsWith("crossbow") && !weapon.contains("Karil")) || weapon.contains("javelin")) {
			return 6;
		}
		if (weapon.contains("Guthan") || weapon.contains("2h") || weapon.contains("halberd") || weapon.contains("Granite maul") || weapon.contains("Tzhaar-ket-om") || weapon.contains("Dharok")) {
			return 7;
		}
		if (weapon.contains("ogre")) {
			return 8;
		}
		if (weapon.contains("Dark bow")) {
			return 9;
		}
		return 5;
	}

	public static int getDefenceAnimation(Player p) {
		int weaponId = p.getEquipment().getItemInSlot(3);
		int shield = p.getEquipment().getItemInSlot(5);
		String weapon = p.getEquipment().getSlot(3).getDefinition().getName();
		if (shield != -1) {
			String shieldName = p.getEquipment().getSlot(5).getDefinition().getName();
			if (shield >= 8844 && shield <= 8850) { // Defenders
				return 4177;
			}
			if (shieldName.contains("book") || shieldName.contains("Book")) {
				return 404;
			}
			return 1156;
		}
		if (weaponId <= 0) {
			return 424;
		}
		if (weapon.contains("xil-ul")) {
			return 425;
		}
		if (weapon.endsWith("whip")) {
			return 1659;
		}
		if (weapon.contains("Granite maul")) {
			return 1666;
		}
		if (weapon.contains("Dharok") || weapon.contains("flail")) {
			return 2063;
		}
		if (weapon.contains("shortbow") || weapon.contains("longbow") || weapon.contains("Karil") || weapon.contains("Crystal") || weapon.contains("Dark bow")) {
			return 425;
		}
		if (weapon.contains("2h") || weapon.contains("godsword") || weapon.contains("Saradomin sword")) {
			return 7050;
		}
		if (weapon.contains("staff") || weapon.contains("Staff") || weapon.contains("halberd")
			|| weapon.contains("warspear") || weapon.contains("spear")) {
			return 420;
		}
		if (weapon.contains("claws")) {
			return 4177;
		}
		if (weapon.contains("wand") || weapon.contains("longsword") || weapon.endsWith("_sword")
			|| weapon.contains("battleaxe") || weapon.contains("mace") || weapon.contains("scimitar") 
			|| weapon.contains("axe") || weapon.contains("warhammer") || weapon.contains("dagger")) {
			return 397;
		}
		return 404;
	}

	public static int getNPCHitDelay(NPC npc) {
		switch(npc.getId()) {

		}
		return 450;
	}

	public static int getPlayerHitDelay(Player p) {
		switch(p.getEquipment().getItemInSlot(3)) {

		}
		return 400;
	}
	
	public static int getStandAnim(Player p) {
		int id = p.getEquipment().getItemInSlot(3);
		String weapon = ItemDefinition.forId(id).getName();
		if (weapon.contains("Dharok")) {
			return 2065;
		}
		if (weapon.contains("flail")) {
			return 2061;
		}
		if (weapon.contains("Karil")) {
			return 2074;
		} 
		if (weapon.contains("Tzhaar-ket-om")) {
			return 0x811;
		}
		if (weapon.equals("Saradomin staff") || weapon.equals("Guthix staff") || weapon.equals("Zamorak staff")) {
			return 0x328;
		}
		if (weapon.contains("Guthan") || weapon.endsWith("spear") || weapon.endsWith("halberd") || weapon.contains("Staff") || weapon.contains("staff") || weapon.contains("wand") || weapon.contains("Dragon longsword") || weapon.equals("Void knight mace")) {
			return 809;
		}
		if (weapon.contains("2h") || weapon.endsWith("godsword") || weapon.equals("Saradomin sword")) {
			return 7047;
		}
		if (weapon.equals("Abyssal whip")) {
			return 10080;
		}
		if (weapon.contains("Granite maul")) {
			return 1662;
		}
		return 808;
	}
	
	public static int getWalkAnim(Player p) {
		int id = p.getEquipment().getItemInSlot(3);
		String weapon = ItemDefinition.forId(id).getName();
		if(weapon.equals("Saradomin staff") || weapon.equals("Guthix staff") || weapon.equals("Zamorak staff")) {
			return 0x333;
		}
		if (weapon.contains("flail")) {
			return 2060;
		}
		if (weapon.contains("Karil")) {
			return 2076;
		}
		if (weapon.contains("Granite maul")) {
			return 1663;
		}
		if (weapon.equals("Abyssal whip")) {
			return 1660;
		}
		if (id == 4718 || weapon.endsWith("2h sword") || weapon.contains("Tzhaar-ket-om")|| weapon.endsWith("godsword") || weapon.equals("Saradomin sword")) {
			return 7046;
		}
		if (weapon.contains("Guthan")|| weapon.contains("spear") || weapon.endsWith("halberd") || weapon.contains("Staff") || weapon.contains("staff") || weapon.contains("wand") || weapon.equals("Void knight mace")) {
			return 1146;
		}
		return 819;
	}
	
	public static int getRunAnim(Player p) {
		int id = p.getEquipment().getItemInSlot(3);
		String weapon = ItemDefinition.forId(id).getName();
		if (weapon.contains("Dharok") || weapon.endsWith("2h sword") || weapon.contains("Tzhaar-ket-om") || weapon.endsWith("godsword") || weapon.equals("Saradomin sword")) {
			return 7039;
		}
		if (weapon.equals("Abyssal whip")) {
			return 1661;
		}
		if (weapon.contains("Granite maul")) {
			return 1664;
		}
		if (weapon.contains("flail")) {
			return 1831;
		}
		if (weapon.contains("Karil")) {
			return 2077;
		}
		if(weapon.contains("Saradomin staff") || weapon.contains("Guthix staff") || weapon.contains("Zamorak staff") || weapon.equals("Void knight mace")) {
			return 824;
		}
		if (weapon.contains("staff") || weapon.contains("Staff") || weapon.contains("halberd")
			|| weapon.contains("wand") || weapon.contains("Dragon longsword") || weapon.contains("warspear")) {
			return 1210;
		}
		return 824;
	}
}
