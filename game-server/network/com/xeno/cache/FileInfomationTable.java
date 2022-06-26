package com.xeno.cache;

import java.nio.ByteBuffer;

/**
 * 
 *
 * @author Flamable
 */
public class FileInfomationTable {

	public int fit_revision;
	private byte protocol;

	public FileInfomationTable(Cache cache, int idx) {
		try {
			ArchiveFile af = cache.getFileSystem().getArchievedFile(255, idx);
			if (af.getBytes() == null || af.getBytes().length < 10) {
				return;
			}

			final ByteBuffer buffer = ByteBuffer.wrap(af.decompress());

			protocol = buffer.get();
			if (protocol >= 6)
				fit_revision = buffer.getInt();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
