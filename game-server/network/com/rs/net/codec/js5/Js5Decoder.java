package com.rs.net.codec.js5;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * 
 *
 * @author Flamable
 */
public class Js5Decoder extends CumulativeProtocolDecoder {

	private final ObjectArrayList<Js5FileRequest> lowPri = new ObjectArrayList<Js5FileRequest>();

	@Override
	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			if(4 <= in.remaining()) {
				while (in.remaining() >= 4) {
					final int pri = in.get() & 0xff;
					final int idxId = in.get() & 0xff;
					final int fileId = in.getShort() & 0xffff;
					if (pri == 1) {
						session.write(new Js5FileRequest(idxId, fileId, pri));
					} else if (pri == 0) {
						lowPri.add(new Js5FileRequest(idxId, fileId, pri));
					} else {
						lowPri.clear();
					}
				}

				for (Object js5 :lowPri.toArray()) {
					session.write(js5);
				}
				lowPri.clear();
				return true;
			}
			in.rewind();
			return false;
		} catch(Exception err) {
			return false;
		}
	}


}
