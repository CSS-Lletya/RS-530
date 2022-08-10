package com.rs.plugin.handler.objects.tes;

import com.rs.plugin.annotations.PluginEventHandler;
import com.rs.plugin.eventbus.ObjectClickEvent;
import com.rs.plugin.handler.objects.ObjectClickHandler;
import com.xeno.net.definitions.ObjectDefinitions;

@PluginEventHandler
public class Snippet {
	
	/**
	 * This is a demo class of a generic farming patch in the spawn area. No actual functionality.
	 */
	public static ObjectClickHandler demo = new ObjectClickHandler(new Object[] { 1278 }) {
		@Override
		public void handle(ObjectClickEvent e) {
			System.out.println(e.getOptionId());
			System.out.println(e.getId());
			System.out.print("zol");
			ObjectDefinitions def = ObjectDefinitions.objectOf(1278);
			if (def == null) {
	            return;
	        }
			System.out.println(def.hasActions());
		}
	};
}
