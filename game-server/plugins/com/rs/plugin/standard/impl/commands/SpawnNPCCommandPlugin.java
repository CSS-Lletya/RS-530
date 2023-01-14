package com.rs.plugin.standard.impl.commands;

import com.rs.entity.actor.npc.NPC;
import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.rs.world.World;

@CommandSignature(alias = {"npc","n"}, rights = {2}, syntax = "Spawn an NPC")
public class SpawnNPCCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	String cmd[] = command.split(" ");
		int id = Integer.valueOf(cmd[1]);
		NPC npc = new NPC(id);
		npc.setLocation(player.getLocation());
		npc.readResolve();
		World.getInstance().getNpcList().add(npc);
    }
}