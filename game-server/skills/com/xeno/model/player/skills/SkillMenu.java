package com.xeno.model.player.skills;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;

/**
 * Represents a Skill Menu
 * @author Dennis
 *
 */
public class SkillMenu {

	/**
	 * An array of menu ids
	 */
	private static final int[] MENU_ID = {
		1, 2, 5, 3, 7, 4, 12, 22, 6, 8, 9, 
		10, 11, 19, 20, 23, 13, 14, 15, 16,
		17, 18, 21, 24
	};
	
	/**
	 * An array of sub menu configurations
	 * (additional options displayed)
	 */
	private static final int[] SUB_CONFIG = {
		0, 1024, 2048, 3072, 4096, 5120, 6144, 7168, 
		8192, 9216, 10240, 11264, 12288, 13312, 14355
	};
	
	/**
	 * Represents the Menu Id itself
	 */
	private static AtomicInteger menuId = new AtomicInteger();
	
	/**
	 * Displays a Skill Menu
	 * @param player
	 * @param buttonId
	 */
	public static void display(Player player, int buttonId) {
		IntStream.range(125, 149).filter(id -> buttonId == id).forEach(menu ->{
			player.getInterfaceManager().displayInterface(499);
			player.getActionSender().sendConfig2(965, MENU_ID[menuId.get()]);
			player.getAttributes().get(Attribute.SKILL_MENU).set((Integer) MENU_ID[menuId.get()]);
			menuId.getAndIncrement();
		});
	}
	
	/**
	 * send the sub menu configuration(s).
	 * @param player
	 * @param buttonId
	 */
	public static void subMenu(Player player, int buttonId) {
		int menu = player.getAttributes().get(Attribute.SKILL_MENU).getInt(); 
		player.getActionSender().sendConfig2(965, SUB_CONFIG[buttonId - 10] + menu);
	}

}