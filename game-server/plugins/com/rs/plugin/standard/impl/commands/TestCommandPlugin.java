package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.content.mapzone.ZoneRestriction;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.getCurrentMapZone().get().addRestriction(ZoneRestriction.FIRES);
    	System.out.println(player.getCurrentMapZone().get().isRestricted(ZoneRestriction.FIRES));
    }
}