package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 667 })
public class EquipmentBonusesInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		if (button == 14) {
			player.getEquipment().unequipItem(button2);
		}
	}
}