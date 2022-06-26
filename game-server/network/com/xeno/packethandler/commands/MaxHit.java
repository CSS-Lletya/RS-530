package com.xeno.packethandler.commands;

import com.xeno.model.player.Player;

public class MaxHit implements Command {

	@Override
	public void execute(Player player, String command) {
		System.out.println("Maxhit = " + player.getMaxHit());
		player.getActionSender().sendMessage("Maxhit = " + player.getMaxHit());
	}

	@Override
	public int minimumRightsNeeded() {
		// TODO Auto-generated method stub
		return 0;
	}

}
