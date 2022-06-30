package com.xeno.net.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xeno.GameConstants;
import com.xeno.entity.actor.player.PlayerCredentials;
import com.xeno.net.Constants;
import com.xeno.net.Packet;
import com.xeno.net.WorkerThread;
import com.xeno.net.codec.js5.Js5CodecFactory;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.utility.LogUtility;
import com.xeno.utility.Utility;
import com.xeno.utility.LogUtility.LogType;

/**
 * Login protocol decoder.
 * @author Graham
 *
 */
public class RS2LoginProtocolDecoder extends CumulativeProtocolDecoder {

	private WorkerThread workerThread;

	public RS2LoginProtocolDecoder(WorkerThread workerThread) {
		this.workerThread = workerThread;
	}

	/**
	 * Parses the data in the provided byte buffer and writes it to
	 * <code>out</code> as a <code>Packet</code>.
	 *
	 * @param session The IoSession the data was read from
	 * @param in	  The buffer
	 * @param out	 The decoder output stream to which to write the <code>Packet</code>
	 * @return Whether enough data was available to create a packet
	 */
	@Override
	public boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {
		try {
			Object loginStageObj = session.getAttribute("LOGIN_STAGE");
			int loginStage = 0;
			if(loginStageObj != null) {
				loginStage = (Integer)loginStageObj;
			}
			if (GameConstants.NETWORK_DEBUG_MODE)
				LogUtility.log(LogType.INFO, "recv login packet, stage: "+loginStage);
			switch(loginStage) {
			case -3:
				StaticPacketBuilder worldList = new StaticPacketBuilder();
				worldList.setBare(true);
				for(int data : Constants.WORLD_LIST_DATA) {
					worldList.addByte((byte) data);
				}
				session.write(worldList.toPacket());
				session.close();
				return true;

			case -1:
				if(3 <= in.remaining()) {
					in.get();
					int clientVersion = in.getShort();
					if(clientVersion == 530) {
						StaticPacketBuilder u1Response = new StaticPacketBuilder();
						u1Response.setBare(true).addByte((byte) 0);
						session.write(u1Response.toPacket());
						//session.setAttribute("LOGIN_STAGE", -2);
						session.getFilterChain().remove("protocolFilter");
						session.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new Js5CodecFactory()));
					}
					return true;
				}
				in.rewind();
				return false;

			case 0: //first login packets
				if(2 <= in.remaining()) {
					int protocolId = in.get() & 0xff;
					@SuppressWarnings("unused")
					int namePart = in.get() & 0xff;
					if(protocolId == 15) {
						session.setAttribute("LOGIN_STAGE", -1);
					} else if(protocolId == 255) {
						session.setAttribute("LOGIN_STAGE", -3);
					} else {
						long serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
						StaticPacketBuilder s1Response = new StaticPacketBuilder();
						s1Response.setBare(true).addByte((byte) 0).addLong(serverSessionKey);
						session.setAttribute("SERVER_SESSION_KEY", serverSessionKey);
						session.write(s1Response.toPacket());
						session.setAttribute("LOGIN_STAGE", 1);
						//Logger.log("protocolId="+protocolId+"; namePart="+namePart);
					}
					return true;
				}
				in.rewind();
				return false;

			case 1: //here's where we get the username and password
				@SuppressWarnings("unused")
				int loginType = -1, loginPacketSize = -1;
				if(3 <= in.remaining()) {
					loginType = in.get() & 0xff; //should be 16 or 18
					loginPacketSize = in.getUnsignedShort();
					//Logger.log("loginType="+loginType);
				} else {
					in.rewind();
					return false;
				}
				if(loginPacketSize >= in.remaining()) {
					byte[] payload = new byte[loginPacketSize];
					in.get(payload);
					Packet p = new Packet(session, -1, payload);
					int loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2); // can't be negative
					int clientVersion = p.readInt();
					p.readByte();
					p.readByte(); // is this still low mem ver?
					p.readByte();
					p.readByte();
					short s1 = p.readShort();
					short s2 = p.readShort();				
					p.readByte();
					for(int n = 0; n < 24; n++) {
						@SuppressWarnings("unused")
						int cachIDX = p.readByte();
					}
					p.readRS2String();
					p.readInt();
					p.readInt();
					p.readShort();
					for(int n = 0; n < 28; n++) {
						@SuppressWarnings("unused")
						int junk = p.readInt();
					}
					int tmpEncryptPacketSize = p.readShort(); //hopefully same as (--loginEncryptPacketSize)
					boolean hd;// = true;
					//if(tmpEncryptPacketSize != 10) {
					//	@SuppressWarnings("unused")
					//int encryptPacketId = p.readByte() & 0xff; //hopefully 10
					hd = false;
					//}

					long clientSessionKey = p.readLong();
					long serverSessionKey = p.readLong();
					String	user = Utility.longToPlayerName(p.readLong()).toLowerCase().trim(), //given username
					pass = p.readRS2String().toLowerCase().trim(); //given password
					int sessionKey[] = new int[4];
					sessionKey[0] = (int)(clientSessionKey >> 32);
					sessionKey[1] = (int)clientSessionKey;
					sessionKey[2] = (int)(serverSessionKey >> 32);
					sessionKey[3] = (int)serverSessionKey;

					/*session.setAttribute("CRYPTION_IN", new ISAACCipher(sessionKey));
					for(int i = 0; i < 4; i++) {
						sessionKey[i] += 50;
					}
					session.setAttribute("CRYPTION_OUT", new ISAACCipher(sessionKey));*/

					session.removeAttribute("LOGIN_STAGE");

					/** 
					 * Here's where we add the user to the login queue, and if the login is 
					 * accepted, we change their session filter to a standard RS2ProtocolCodec.
					 */
					LogUtility.log(LogType.INFO, "Login request: [username = "+user+",password = "+pass+"].");
					PlayerCredentials d = new PlayerCredentials(user, pass, session, hd);
					workerThread.loadPlayer(d);

					session.setIdleTime(IdleStatus.BOTH_IDLE, Constants.SESSION_IDLE_TIME);

					session.getFilterChain().remove("protocolFilter");
					session.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new CodecFactory()));
					return true;
				} else {
					in.rewind();
					return false;
				}
			}
		} catch(Exception e) {
			LogUtility.log(LogType.INFO, e.toString());
		}
		return false;
	}

	static int i = 0;

	/**
	 * Releases the buffer used by the given session.
	 *
	 * @param session The session for which to release the buffer
	 * @throws Exception if failed to dispose all resources
	 */
	@Override
	public void dispose(IoSession session) throws Exception {
		super.dispose(session);
	}

}
