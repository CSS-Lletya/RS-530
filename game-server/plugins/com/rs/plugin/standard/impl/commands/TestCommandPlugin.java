package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.content.mapzone.impl.TestMapZone;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.Skills;
import com.xeno.net.entity.masks.Animation;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	System.out.println(player.getSkills().getTotalLevel());
    	player.getSkills().addXp(Skills.ATTACK, 5000);
    	System.out.println(player.getSkills().getTotalLevel());
    }
}