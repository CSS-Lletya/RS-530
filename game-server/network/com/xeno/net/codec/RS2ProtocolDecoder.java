package com.xeno.net.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xeno.net.Constants;
import com.xeno.net.Packet;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;

public class RS2ProtocolDecoder extends CumulativeProtocolDecoder {
	
	/**
	 * To make sure only the CodecFactory can initialise us.
	 */
	protected RS2ProtocolDecoder() {
	}

	@Override
	/**
	 * Decodes a message.
	 * @param session
	 * @param in
	 * @param out
	 * @return
	 */
	public boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			if(in.remaining() >= 1) {
				// get opcode
				int id = in.get() & 0xFF;
				// get length
				int len = Constants.PACKET_LENGTHS[id];
				if(len == -1) {
					// variable length packet
					if(in.remaining() >= 1) {
						len = in.get() & 0xff;
					} else {
						in.rewind();
						return false;
					}
				}
				if(len < 0) {
					len = in.remaining();
					LogUtility.log(LogType.WARN, "Unkown length: " + id + ", guessed to be: " + len + ".");
					//throw new Exception("Packet length not known: " + id);
				}
				// if we can get the packet then do so
				if (in.remaining() >= len) {
					byte[] payload = new byte[len];
					in.get(payload);
					Packet p = new Packet(session, id, payload);
					out.write(p);
					return true;
				} else {
					in.rewind();
					return false;
				}
			}
			return false;
		} catch(Exception err) {
			LogUtility.log(LogType.INFO, err.toString());
			return false;
		}
	}
	
	@Override
	/**
	 * Releases resources used by this decoder.
	 * @param session
	 */
	public void dispose(IoSession session) throws Exception {
		super.dispose(session);
	}

}
