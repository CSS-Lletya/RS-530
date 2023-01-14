package com.rs.plugin.standard.impl.rsinterfaces;

import com.rs.entity.actor.player.Player;
import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 261 })
public class SettingsInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int button, int button2) throws Exception {
		switch (button) {
		case 16: // Display settings.
			player.getInterfaceManager().displayInterface(742);
			break;

		case 18: // Audio settings.
			player.getInterfaceManager().displayInterface(743);
			break;

		case 3: // Run toggle.
			if (!player.getWalkingQueue().isRunToggled()) {
				player.getWalkingQueue().setRunToggled(true);
				player.getActionSender().sendConfig(173, 1);
			} else {
				player.getWalkingQueue().setRunToggled(false);
				player.getActionSender().sendConfig(173, 0);
			}
			break;

		case 4: // Chat effect toggle.
			if (!player.getPlayerDetails().isChat()) {
				player.getPlayerDetails().setChat(true);
				player.getActionSender().sendConfig(171, 0);
			} else {
				player.getPlayerDetails().setChat(false);
				player.getActionSender().sendConfig(171, 1);
			}
			break;

		case 5: // Split private chat toggle.
			if (!player.getPlayerDetails().isSplit()) {
				player.getPlayerDetails().setSplit(true);
				player.getActionSender().sendConfig(287, 1);
			} else {
				player.getPlayerDetails().setSplit(false);
				player.getActionSender().sendConfig(287, 0);
			}
			break;
			
		case 6: // Mouse buttons toggle.
			if (!player.getPlayerDetails().isMouse()) {
				player.getPlayerDetails().setMouse(true);
				player.getActionSender().sendConfig(170, 0);
			} else {
				player.getPlayerDetails().setMouse(false);
				player.getActionSender().sendConfig(170, 1);
			}
			break;
			
		case 7: // Accept aid toggle.
			if (!player.getPlayerDetails().isAid()) {
				player.getPlayerDetails().setAid(true);
				player.getActionSender().sendConfig(427, 1);
			} else {
				player.getPlayerDetails().setAid(false);
				player.getActionSender().sendConfig(427, 0);
			}
			break;
		case 8:
			player.getActionSender().sendMessage("House options..");
			break;
		}
	}
}