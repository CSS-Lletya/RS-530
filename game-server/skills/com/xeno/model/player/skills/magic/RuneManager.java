package com.xeno.model.player.skills.magic;

import com.xeno.entity.actor.player.Player;

public class RuneManager extends MagicData {

	public RuneManager() {
		
	}
	
	public static boolean hasRunes(Player p, int[] runes, int[] amount) {
		for (int i = 0; i < runes.length; i++) {
			if (checkForStaffs(p, runes[i]) != -1) {
				if (!p.getInventory().hasItemAmount(runes[i], amount[i])) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static int checkForStaffs(Player p, int rune) {
		for (int i = 0 ; i < RUNE_REPLACER.length; i++) {
			if (p.getEquipment().getItemInSlot(3) == RUNE_REPLACER[i]) {
				if (rune == REPLACABLE_RUNES[i]) {
					rune = -1;
					break;
				}
			}
		}
		return rune;
	}

	public static boolean deleteRunes(Player p, int[] runes, int[] amount) {
		for (int i = 0; i < runes.length; i++) {
			if (checkForStaffs(p, runes[i]) != -1) {
				if (!p.getInventory().deleteItem(runes[i], amount[i])) {
					return false;
				}
			}
		}
		return true;
	}
}
