package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 387 })
public class EquipmentInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		switch (button) {
		case 55: // Character display.
			player.getEquipment().displayEquipmentScreen();
			break;

		case 52: // Items kept on death.
			System.out.println("No longer existant");
			break;
		}
	}
}