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
			xstream.alias("player", com.xeno.entity.actor.player.Player.class);
			xstream.alias("itemDefinition", com.xeno.net.definitions.ItemDefinition.class);
			xstream.alias("item", com.xeno.entity.actor.item.Item.class);
			xstream.alias("npcDefinition", com.xeno.net.definitions.NPCDefinition.class);
			xstream.alias("npc", com.xeno.entity.actor.npc.NPC.class);
			xstream.alias("shop", com.xeno.content.Shop.class);
			xstream.alias("npcDrop", com.xeno.entity.actor.npc.NPCDrop.class);
			xstream.alias("door", com.xeno.content.DoorControl.Door.class);
			xstream.alias("doorControl", com.xeno.content.DoorControl.class);
		}
		return xstream;
	}

}
