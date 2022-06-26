package com.xeno.content.emote;

import com.xeno.entity.player.Bank;
import com.xeno.entity.player.Player;

/**
 * 
 * @author Thomas
 *
 */

public class SkillCapes {

	Player player;
	
	public SkillCapes(Player player) {
		this.player = player;
	}
	
	private int[][] SKILLCAPES = { {9747, 9748}, {9753, 9754} };
	
	public void updateSkillCape(int skill) {
		if (get99SkillCount() > 1) {
			for (int i = 0; i < SKILLCAPES.length; i++) {
				if (player.getBank().hasItem(SKILLCAPES[i][0])) {
					for (int slot = 0; slot < Bank.MAX_SLOTS; slot++) {
						if (player.getBank().getBank()[slot].getItemId() == SKILLCAPES[i][0]) {
							player.getBank().getBank()[slot].setItemId(SKILLCAPES[i][1]);
						}
					}
				}
				int amountOfCapesInInvy = 0;
				if (player.getInventory().hasItem(SKILLCAPES[i][0])) {
					amountOfCapesInInvy = player.getInventory().getItemAmount(SKILLCAPES[i][0]);
					player.getInventory().deleteItem(SKILLCAPES[i][0], amountOfCapesInInvy);
					player.getInventory().addItem(SKILLCAPES[i][1], amountOfCapesInInvy);
				}
					player.getEquipment().updateEquipedItem(SKILLCAPES[i][0], SKILLCAPES[i][1]);
			}
			player.getActionSender().sendMessage("Your skillcape magicly turns from untrimmed to trimmed!");
		}
	}
	
	private int get99SkillCount() {
		int skill99Count = 0;
		for (int j = 0; j < player.getLevels().getLevel().length; j++) {
			if (player.getLevels().getLevel()[j] >= 99) {
				skill99Count++;
			}
		}
		return skill99Count;
	}
	
}
