package com.xeno.util;

import com.thoughtworks.xstream.XStream;

/**
 * Util class to get the xstream object.
 * @author Graham
 *
 */
public class XStreamUtil {
	
	private XStreamUtil() {}
	
	private static XStream xstream = null;
	
	public static XStream getXStream() {
		if(xstream == null) {
			xstream = new XStream();
			/*
			 * Set up our aliases.
			 */
			xstream.alias("packet", com.xeno.packethandler.PacketHandlerDef.class);
			xstream.alias("player", com.xeno.model.player.Player.class);
			xstream.alias("itemDefinition", com.xeno.model.ItemDefinition.class);
			xstream.alias("item", com.xeno.model.Item.class);
			xstream.alias("npcDefinition", com.xeno.model.npc.NPCDefinition.class);
			xstream.alias("npc", com.xeno.model.npc.NPC.class);
			xstream.alias("shop", com.xeno.world.Shop.class);
			xstream.alias("npcDrop", com.xeno.model.npc.NPCDrop.class);
			xstream.alias("door", com.xeno.world.DoorControl.Door.class);
			xstream.alias("doorControl", com.xeno.world.DoorControl.class);
		}
		return xstream;
	}

}
