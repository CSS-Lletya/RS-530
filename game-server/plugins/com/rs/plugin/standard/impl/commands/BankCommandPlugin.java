package com.rs.plugin.standard.impl.commands;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.player.Player;

@CommandSignature(alias = {"bank"}, rights = {2}, syntax = "Opens Bank")
public final class BankCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] parts, String command) {
    	player.getBank().openBank();
    }
}