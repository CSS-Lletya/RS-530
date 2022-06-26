package com.xeno.packethandler.commands;

import com.xeno.model.World;
import com.xeno.model.player.Player;

public class Uptime implements Command {

	@Override
	public void execute(Player player, String command) {
		player.getActionSender().sendMessage("Xenorune uptime : " + getTime());
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}
	
	// Thanks to scu11 for help with the days
	public static String getTime() {
		long l = System.currentTimeMillis() - World.getInstance().getServerStartupTime();
		long days = (l / 86400000L);
		long hours = ((l / 3600000L) % 24L);
		long minutes = ((l / 60000L) % 60L);
		long seconds = ((l / 1000L) % 60L);
		String string = "";
		if (days > 0) {
			String s = days == 1 ? " day, " : " days, ";
			string += days + s;
		}
		if (hours > 0) {
			String s = hours == 1 ? " hour, " : " hours, ";
			string += hours + s;
		}
		if (minutes > 0) {
			String s = minutes == 1 ? " minute, " : " minutes, ";
			string += minutes + s;
		}
		string += seconds + " seconds.";
		return string;
	}
	
}
