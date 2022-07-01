package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.player.Player;

@CommandSignature(alias = {"t","test"}, rights = {0}, syntax = "A dummy test command")
public final class TestCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	System.out.println(player.getAttributes().exist(Attribute.LOCKED));
    	player.getAttributes().get(Attribute.LOCKED).set(true);
    	System.out.println(player.getAttributes().exist(Attribute.LOCKED));
    }
}