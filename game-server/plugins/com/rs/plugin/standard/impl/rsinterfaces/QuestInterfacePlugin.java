package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.entity.actor.player.Player;

//TODO: Fix, doesn't seem to work properly on resize hd mode for some reason.
@RSInterfaceSignature(interfaceId = {274, 259})
public class QuestInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		if (interfaceId == 274)
			switch(button) {
			case 3: // Achievment diary toggle.
				player.getInterfaceManager().sendTab(85, 259);
				player.getPlayerDetails().setAchievementDiaryTab(true);
				break;
		}
		else
			switch(button) {
			case 8: // Quest tab toggle.
				player.getInterfaceManager().sendTab(85, 274);
				player.getPlayerDetails().setAchievementDiaryTab(false);
				break;
		}
	}
}