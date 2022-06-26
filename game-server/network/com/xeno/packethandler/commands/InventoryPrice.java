package com.xeno.packethandler.commands;

import java.text.NumberFormat;

import com.xeno.entity.player.Player;

public class InventoryPrice implements Command {

	@Override
	public void execute(Player player, String command) {
		int price = 0;
		for (int i = 0; i < player.getInventory().getItems().length; i++) {
			if (player.getInventory().getItemInSlot(i) > 0) {
				price += (player.getInventory().getSlot(i).getDefinition().getPrice().getNormalPrice() * player.getInventory().getAmountInSlot(i));
				if (player.getInventory().getItemInSlot(i) == 995) {
					price += player.getInventory().getAmountInSlot(i);
				}
			}
		}
		player.getActionSender().sendMessage("Value of inventory: " + NumberFormat.getInstance().format(price));
	}

	@Override
	public int minimumRightsNeeded() {
		// TODO Auto-generated method stub
		return 0;
	}

}
