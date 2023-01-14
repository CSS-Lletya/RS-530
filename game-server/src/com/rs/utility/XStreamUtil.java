package com.rs.utility;

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
			xstream.alias("player", com.rs.entity.actor.player.Player.class);
			xstream.alias("itemDefinition", com.rs.net.definitions.ItemDefinition.class);
			xstream.alias("item", com.rs.entity.actor.item.Item.class);
			xstream.alias("npcDefinition", com.rs.net.definitions.NPCDefinition.class);
			xstream.alias("npc", com.rs.entity.actor.npc.NPC.class);
			xstream.alias("shop", com.rs.content.Shop.class);
		}
		return xstream;
	}

}
