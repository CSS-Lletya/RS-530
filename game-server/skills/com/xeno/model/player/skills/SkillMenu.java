package com.xeno.model.player.skills;

import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;

public class SkillMenu {
	
	public SkillMenu() {
		
	}

	private static final int[] MENU_ID = {
		1, 2, 5, 3, 7, 4, 12, 22, 6, 8, 9, 
		10, 11, 19, 20, 23, 13, 14, 15, 16,
		17, 18, 21, 24
	};
	
	private static final int[] SUB_CONFIG = {
		0, 1024, 2048, 3072, 4096, 5120, 6144, 7168, 
		8192, 9216, 10240, 11264, 12288, 13312, 14355
	};
	
	public static void display(Player player, int buttonId) {
		int j = 0;
		for (int i = 125; i < 149; i++) {
			if (buttonId == i) {
				player.getInterfaceManager().displayInterface(499);
				player.getActionSender().sendConfig2(965, MENU_ID[j]);
				player.getAttributes().get(Attribute.SKILL_MENU).set((Integer) MENU_ID[j]);
				break;
			}
			j++;
		}
	}
	
	public static void subMenu(Player player, int buttonId) {
		int menu = player.getAttributes().get(Attribute.SKILL_MENU).getInt(); 
		player.getActionSender().sendConfig2(965, SUB_CONFIG[buttonId - 10] + menu);
	}

}