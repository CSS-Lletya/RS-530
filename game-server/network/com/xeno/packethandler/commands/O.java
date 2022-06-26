package com.xeno.packethandler.commands;

import com.xeno.content.combat.CombatFormula;
import com.xeno.entity.npc.NPC;
import com.xeno.entity.player.Player;

public class O implements Command {
	
	@Override
	public void execute(final Player player, String command) {
		String cmd[] = command.split(" ");
		player.getActionSender().sendMessage("ATT = " + (int)CombatFormula.getMeleeAttack(player) + " DEF = " + (int)CombatFormula.getMeleeDefence(player, player) + " SPEC = " + (int)CombatFormula.getMeleeAttack(player) * CombatFormula.getSpecialAttackBonus(player.getEquipment().getItemInSlot(3)));
		int id = Integer.parseInt(cmd[1]);
		player.getActionSender().sendMessage("NPC ATT = " + (int)CombatFormula.getNPCMeleeAttack(new NPC(id)) + " NPC DEF = " + (int)CombatFormula.getNPCMeleeDefence(new NPC(id)));
		//player.getLevels().setLevel(3, id);
		//player.getActionSender().sendMessage(""+player.getMaxHit());
	}

	@Override
	public int minimumRightsNeeded() {
		return 0;
	}

}
