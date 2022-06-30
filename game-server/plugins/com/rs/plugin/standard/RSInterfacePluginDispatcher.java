package com.rs.plugin.standard;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.plugin.standard.listener.RSInterface;
import com.rs.plugin.standard.wrapper.RSInterfaceSignature;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.Packet;
import com.xeno.utility.Utility;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

/**
 * @author Dennis
 */
public final class RSInterfacePluginDispatcher {

	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectArrayMap<RSInterfaceSignature, RSInterface> INTERFACES = new Object2ObjectArrayMap<>();

	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	public static void execute(Player player, int interfaceId, int button, int button2) {
		getRSInterface(interfaceId).ifPresent(inter -> {
			Try.run(() -> inter.execute(player, interfaceId, button, button2));
		});
	}

	/**
	 * Gets a interface which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<RSInterface> getRSInterface(int interfaceId) {
		for (Entry<RSInterfaceSignature, RSInterface> rsInterface : INTERFACES.entrySet()) {
			if (isInterface(rsInterface.getValue(), interfaceId)) {
				return Optional.of(rsInterface.getValue());
			}
		}
		return Optional.empty();
	}

	private static boolean isInterface(RSInterface rsInterface, int interfaceId) {
		Annotation annotation = rsInterface.getClass().getAnnotation(RSInterfaceSignature.class);
		RSInterfaceSignature signature = (RSInterfaceSignature) annotation;
		return Arrays.stream(signature.interfaceId()).anyMatch(right -> interfaceId == right);
	}

	/**
	 * Loads all the interface into the {@link #INTERFACES} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<RSInterface> interfaces = Utility.getClassesInDirectory("com.rs.plugin.standard.impl.rsinterfaces").stream()
				.map(clazz -> (RSInterface) clazz).collect(Collectors.toList());
		interfaces.forEach(inter -> INTERFACES.put(inter.getClass().getAnnotation(RSInterfaceSignature.class), inter));
	}

	/**
	 * Reloads all the interface into the {@link #INTERFACES} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		INTERFACES.clear();
		load();
	}

	public static void handleButtons(final Player player, Packet stream, int packetId) {

	}
}