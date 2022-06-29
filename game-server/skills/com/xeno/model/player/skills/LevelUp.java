package com.xeno.model.player.skills;

import com.xeno.entity.actor.player.Player;
import com.xeno.utility.Utility;

/**
 * Level up class.
 * @author Graham
 * @author Luke132
 *
 */
public class LevelUp {
	
	public static final int[] SKILL_ICON = {
		100000000, 400000000, 200000000, 450000000, 250000000, 500000000,
		300000000, 1100000000, 1250000000, 1300000000, 1050000000, 1200000000,
		800000000, 1000000000, 900000000, 650000000, 600000000, 700000000,
		1400000000, 1450000000, 850000000, 1500000000, 1600000000, 1650000000, 0,
	};
	
	public static final int[] SKILL_FLASH = {
		1, 4, 2, 64, 8, 16, 32, 32768, 131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128,
		512, 524288, 1048576, 262144, 2097152, 4194304, 8388608, 0,
	};

	public static void levelUp(Player player, int skill) {
		String s = "<br><br><br>";
		String s1 = "<br><br><br><br>";
		if (player.getTemporaryAttribute("teleporting") == null) {
			player.graphics(199, 0, 100);
		}
		player.getActionSender().sendMessage("You've just advanced a " + Utility.SKILL_NAME[skill] + " level! You have reached level " + player.getLevels().getLevelForXp(skill) + ".");
		player.getActionSender().modifyText(s + "Congratulations, you have just advanced a " + Utility.SKILL_NAME[skill] + " level!", 740, 0);
		player.getActionSender().modifyText(s1 + "You have now reached level " + player.getLevels().getLevelForXp(skill) + ".", 740, 1);
		player.getActionSender().modifyText("", 740, 2);
		player.getActionSender().sendConfig2(1179, SKILL_ICON[skill]);
		player.getActionSender().sendConfig2(1179, SKILL_FLASH[skill]);
		player.getActionSender().sendChatboxInterface2(740);
	}

}
