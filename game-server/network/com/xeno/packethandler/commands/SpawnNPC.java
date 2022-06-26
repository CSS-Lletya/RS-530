package com.xeno.packethandler.commands;

import com.xeno.model.World;
import com.xeno.model.npc.NPC;
import com.xeno.model.player.Player;

public class SpawnNPC implements Command {

	@Override
	public void execute(Player player, String command) {
		String cmd[] = command.split(" ");
		int id = Integer.valueOf(cmd[1]);
		NPC npc = new NPC(id);
		npc.setLocation(player.getLocation());
		npc.readResolve();
		World.getInstance().getNpcList().add(npc);
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
