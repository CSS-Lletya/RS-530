package com.rs.cache;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import com.rs.util.cache.CBZip2InputStream;

/**
 * 
 *
 * @author Flamable
 */
public class ArchiveFile {

	private final int idx;
	private final int archiveID;
	private ByteBuffer fileBuffer;
	private final int compression;
	private final int uncompressedSize;
	private final int realLen;

	public ArchiveFile(int cache, int file, ByteBuffer buffer) {
		this.idx = cache;
		this.archiveID = file;
		this.fileBuffer = buffer;
		compression = buffer.get();
		uncompressedSize = buffer.getInt();
		realLen = getCompression() != 0 ? buffer.getInt() : getUncompressedSize();
	}
	
	public byte[] decompress() throws Exception {
		if (realLen < 0 || realLen > 100000000) {
			throw new Exception();
		} else {
			byte[] data = new byte[realLen];
			switch(getCompression()){
			case 0:
				System.arraycopy(getBytes(), 5, data, 0, realLen);
				break;
			case 1:
				bzip2Decompress(uncompressedSize, 9, getBytes(), data);
				break;
			default:
				gZipDecompress(uncompressedSize, 9, getBytes(), data);
			break;
			}
			return data;
		}
	}
	
	public static void gZipDecompress(int slen,int off,byte[] in,byte[] out) throws IOException {
		byte in2[] = new byte[slen];
		System.arraycopy(in,off,in2,0,slen);
		DataInputStream ins = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(in2)));
		ins.readFully(out);
		ins.close();
	}

	public static void bzip2Decompress(int slen,int off,byte[] in,byte[] out) throws IOException {
		byte in2[] = new byte[slen];
		System.arraycopy(in,off,in2,0,slen);
		DataInputStream ins = new DataInputStream(new CBZip2InputStream(new ByteArrayInputStream(in2),0));
		ins.readFully(out);
		ins.close();
	}

	public int getIdx() {
		return idx;
	}

	public int getArchieveId() {
		return archiveID;
	}

	public ByteBuffer getBuffer() {
		return fileBuffer;
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[fileBuffer.limit()];
		fileBuffer.position(0);
		fileBuffer.get(bytes);
		return bytes;
	}

	public int getCompression() {
		return compression;
	}

	public int getUncompressedSize() {
		return uncompressedSize;
	}

	public int getRealLen() {
		return realLen;
	}

}
