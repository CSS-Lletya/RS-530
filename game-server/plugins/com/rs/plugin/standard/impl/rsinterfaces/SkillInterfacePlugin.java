package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.entity.actor.player.Player;
import com.xeno.model.player.skills.SkillMenu;

@RSInterfaceSignature(interfaceId = {320, 499})
public class SkillInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		if (interfaceId == 320)
			SkillMenu.display(player, button);
		else
			SkillMenu.subMenu(player, button);
	}
}