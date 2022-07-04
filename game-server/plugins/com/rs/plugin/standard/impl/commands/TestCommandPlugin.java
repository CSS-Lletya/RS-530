package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.player.Player;
import com.xeno.utility.ZoneBorders;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	ZoneBorders border = new ZoneBorders(2824, 5296, 2842, 5308);
    	System.out.println(border.insideBorder(player));
    }
}