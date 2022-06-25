package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;
import com.xeno.model.player.Skills;

public class Master implements Command {

	@Override
	public void execute(Player player, String command) {
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			player.getLevels().setLevel(i, 99);
			player.getLevels().setXp(i, player.getLevels().getXpForLevel(99));
		}
		player.getActionSender().sendSkillLevels();
		player.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
