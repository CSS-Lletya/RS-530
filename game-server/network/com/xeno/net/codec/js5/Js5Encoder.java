package com.xeno.net.codec.js5;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.xeno.cache.ArchiveFile;
import com.xeno.net.Server;

/**
 * 
 *
 * @author Flamable
 */
public class Js5Encoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(IoSession session, Object object, ProtocolEncoderOutput out)
	throws Exception {
		if (object instanceof Js5FileRequest) {
			Js5FileRequest js5 = (Js5FileRequest) object;
			ArchiveFile af = Server.getCache().getFileSystem().getArchievedFile(js5.getIdxId(), js5.getFileId());
			if (af == null) {
				out.write(ByteBuffer.allocate(5));
				return;
			}

			int attributes = af.getCompression();
			if (js5.getPriority() == 0) {
				attributes |= 0x80;
			}
			byte[] cachePayload = new byte[af.getCompression() != 0 ? af.getUncompressedSize() + 4: af.getUncompressedSize()];
			System.arraycopy(af.getBytes(), 5, cachePayload, 0, cachePayload.length);
			
			ByteBuffer bb = ByteBuffer.allocate(cachePayload.length + (cachePayload.length / 256) + 8);

			bb.put((byte) js5.getIdxId());
			bb.putShort((short) js5.getFileId());
			bb.put((byte) attributes);
			bb.putInt(af.getUncompressedSize());
			
			int offset = 8;
			for (int i = 0; i < cachePayload.length; i++) {
				if (offset == 512) {
					bb.put((byte) 255);
					offset = 1;
				}
				bb.put(cachePayload[i]);
				offset++;
			}
			bb.flip();
			out.write(bb);
		}
	}

}
