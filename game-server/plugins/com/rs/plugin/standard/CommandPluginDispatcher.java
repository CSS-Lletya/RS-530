package com.rs.plugin.standard;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.plugin.standard.listener.Command;
import com.rs.plugin.standard.wrapper.CommandSignature;
import com.xeno.entity.player.Player;
import com.xeno.util.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CommandPluginDispatcher {

	private static final Object2ObjectOpenHashMap<CommandSignature, Command> COMMANDS = new Object2ObjectOpenHashMap<>();

	public static void execute(Player player, String[] parts, String command) {
		getCommand(parts[0]).ifPresent(commander -> {
			if (!hasPrivileges(player, commander)) {
				player.getActionSender().sendMessage("You don't have the privileges required to use this command.");
				return;
			}
			try {
				commander.execute(player, parts, command);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static Optional<Command> getCommand(String identifier) {
		for (Entry<CommandSignature, Command> command : COMMANDS.entrySet()) {
			for (String s : command.getKey().alias()) {
				if (identifier.equalsIgnoreCase(s)) {
					return Optional.of(command.getValue());
				}
			}
		}
		return Optional.empty();
	}
	
	private static boolean hasPrivileges(Player player, Command command) {
		Annotation annotation = command.getClass().getAnnotation(CommandSignature.class);
		CommandSignature sig = (CommandSignature) annotation;
		if (player.getRights() > 0) {
			return true;
		}
		return Arrays.stream(sig.rights()).anyMatch(right -> player.getRights() == right);
	}
	
	public static void load() {
		List<Command> commands = Utility.getClassesInDirectory("com.rs.plugin.standard.impl.commands").stream()
				.map(clazz -> (Command) clazz).collect(Collectors.toList());
		commands.forEach(command -> COMMANDS.put(command.getClass().getAnnotation(CommandSignature.class), command));
	}
	
	public static void reload() {
		COMMANDS.clear();
		load();
	}
}