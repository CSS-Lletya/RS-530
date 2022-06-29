package com.xeno.net;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.xeno.GameEngine;
import com.xeno.GameLoader;
import com.xeno.entity.actor.player.Player;
import com.xeno.net.codec.CodecFactory;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.world.World;

/**
 * Handles incoming events from MINA.
 * @author Graham
 *
 */
public class ConnectionHandler implements IoHandler {
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * Creates the connection handler.
	 * @param engine
	 */
	public ConnectionHandler(GameEngine engine) {
		this.engine = engine;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		//logger.error("Exception caught: " + session + ": " + throwable.getMessage() + ".");
		//logger.stackTrace(throwable);
	}

	@Override
	public void messageReceived(IoSession session, Object data) throws Exception {
		Packet packet = (Packet) data;
		Player player = (Player) session.getAttachment();
		player.addPacketToQueue(packet);
	}

	@Override
	public void messageSent(IoSession session, Object data) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(session.getAttachment() != null) {
			World.getInstance().unregister((Player) session.getAttachment());
		}
		LogUtility.log(LogType.INFO, "Session has been closed: " + session.getRemoteAddress().toString());
		// TODO remove player from lists here
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		LogUtility.log(LogType.INFO, "Session is now idle: " + session.getRemoteAddress().toString());
		session.close();
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LogUtility.log(LogType.INFO, "New session from: " + session.getRemoteAddress().toString());
		session.setIdleTime(IdleStatus.BOTH_IDLE, Constants.SESSION_INITIAL_IDLE_TIME);
		session.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new CodecFactory(engine.getLoader().getWorkerThread())));
	}

}
