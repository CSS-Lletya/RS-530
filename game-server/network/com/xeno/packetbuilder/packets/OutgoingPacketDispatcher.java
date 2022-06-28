package com.xeno.packetbuilder.packets;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * @author Dennis
 */
public class OutgoingPacketDispatcher {

	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectOpenHashMap<OutgoingPacketSignature, OutgoingPacket> PACKET = new Object2ObjectOpenHashMap<>();

	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	public static void execute(Player player, Packet input, int packetId) {
		Optional<OutgoingPacket> incomingPacket = getVerifiedPacket(packetId);
		incomingPacket.ifPresent(packet -> packet.execute(player, input));
	
	}

	/**
	 * Gets a interface which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<OutgoingPacket> getVerifiedPacket(int id) {
		for (Entry<OutgoingPacketSignature, OutgoingPacket> incomingPacket : PACKET.entrySet()) {
			if (isPacket(incomingPacket.getValue(), id)) {
				return Optional.of(incomingPacket.getValue());
			}
		}
		return Optional.empty();
	}

	private static boolean isPacket(OutgoingPacket incomingPacket, int packetId) {
		Annotation annotation = incomingPacket.getClass().getAnnotation(OutgoingPacketSignature.class);
		OutgoingPacketSignature signature = (OutgoingPacketSignature) annotation;
		return Arrays.stream(signature.packetId()).anyMatch(packet -> packet == packetId);
	}

	/**
	 * Loads all the interface into the {@link #PACKET} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<OutgoingPacket> commands = Utility.getClassesInDirectory("com.xeno.packetbuilder.packets.impl").stream()
				.map(clazz -> (OutgoingPacket) clazz).collect(Collectors.toList());
		commands.forEach(command -> PACKET.put(command.getClass().getAnnotation(OutgoingPacketSignature.class), command));
	}

	/**
	 * Reloads all the interface into the {@link #PACKET} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		PACKET.clear();
		load();
	}
}