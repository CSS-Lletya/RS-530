package com.xeno.packethandler.commands;

import java.util.HashMap;
import java.util.Map;

import com.xeno.entity.player.Player;

/**
 * 
 * Command manager.
 * @author Graham
 * @author Luke132
 */
public class CommandManager {

	private static Map<String, Command> commandMap = new HashMap<String, Command>();
	static {
		commandMap.put("item", new Pickup());
		commandMap.put("gfx", new Graphic());
		commandMap.put("emote", new Animation());
		commandMap.put("tele", new Teleport());
		commandMap.put("inter", new Interface());
		commandMap.put("under", new Underground());
		commandMap.put("above", new AboveGround());
		commandMap.put("height", new Height());
		commandMap.put("coords", new Coordinates());
		commandMap.put("bank", new Bank());
		commandMap.put("npc", new SpawnNPC());
		commandMap.put("shop", new OpenShop());
		commandMap.put("update", new SystemUpdate());
		commandMap.put("obj", new SpawnObject());
		commandMap.put("char", new CharacterAppearance());
		commandMap.put("empty", new EmptyInventory());
		commandMap.put("setlevel", new SetLevel());
		commandMap.put("kick", new Kick());
		commandMap.put("kickall", new KickAll());
		commandMap.put("config", new TestConfig());
		commandMap.put("switch", new SwitchMagic());
		commandMap.put("pnpc", new PlayerAsNPC());
		commandMap.put("o", new O());
		commandMap.put("maxhit", new MaxHit());
		commandMap.put("master", new Master());
		commandMap.put("spec", new RestoreSpecial());
		commandMap.put("value", new InventoryPrice());
		commandMap.put("uptime", new Uptime());
		commandMap.put("players", new Players());
		commandMap.put("yell", new Yell());
	}
	
	public static void execute(Player player, String command) {
		String name = "";
		if (command.indexOf(' ') > -1) {
			name = command.substring(0, command.indexOf(' '));
		} else {
			name = command;
		}
		name = name.toLowerCase();
		if (commandMap.get(name) != null) {
			if (player.getRights() >= commandMap.get(name).minimumRightsNeeded()) {
				commandMap.get(name).execute(player, command);
			}
		}
	}
}
