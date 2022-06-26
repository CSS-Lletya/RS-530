package com.xeno.packethandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.common.IoSession;

import com.thoughtworks.xstream.XStream;
import com.xeno.entity.player.Player;
import com.xeno.net.Packet;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.util.XStreamUtil;

/**
 * Wholly static class used to handle packets.
 * @author Graham
 *
 */
public class PacketHandlers {
	
	/**
	 * Packet handlers map.
	 */
	private static Map<Integer, PacketHandler> handlers;
	
	/**
	 * Prevent an instance being created.
	 */
	private PacketHandlers() {}
	
	/**
	 * Loads the packet handlers.
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static void loadHandlers() throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		handlers = new HashMap<Integer, PacketHandler>();
		XStream xstream = XStreamUtil.getXStream();
		
		LogUtility.log(LogType.INFO, "Packet handlers:");
		List<PacketHandlerDef> defs = (List<PacketHandlerDef>) xstream.fromXML(new FileInputStream("data/packetHandlers.xml"));
		for(PacketHandlerDef def : defs) {
			
			PacketHandler handler = (PacketHandler) Class.forName(def.handler).newInstance();
			
			String binds = "[ ";
			for(int bind : def.binds) {
				binds += bind + ", ";
				handlers.put(bind, handler);
			}
			binds = binds.substring(0, binds.length()-2);
			binds += " ]";
			LogUtility.log(LogType.INFO, "\tPacket " + handler + " bound to: " + binds);
		}
	}
	
	/**
	 * Handles a packet.
	 * @param session
	 * @param p
	 */
	public static void handlePacket(IoSession session, Packet p) {
		if(!p.isBare()) {
			PacketHandler handler = handlers.get(p.getId());
			if(handler == null) {
				LogUtility.log(LogType.ERROR, "Unhandled packet: " + p + ".");
			} else {
				handler.handlePacket((Player) session.getAttachment(), session, p);
			}
		}
	}

}
