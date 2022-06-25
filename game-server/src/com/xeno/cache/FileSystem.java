package com.xeno.cache;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * 
 *
 * @author Flamable
 */
public class FileSystem {

	private final Cache cache;
	private final RandomAccessFile dataFile;
	private final RandomAccessFile[] indexFiles;
	private final RandomAccessFile idx255IndexFile;
	

	public FileSystem(Cache cache, RandomAccessFile dataFile,
			RandomAccessFile[] indexFiles, RandomAccessFile idx255IndexFile) {
		this.cache = cache;
		this.dataFile = dataFile;
		this.indexFiles = indexFiles;
		this.idx255IndexFile = idx255IndexFile;
	}

	public synchronized ArchiveFile getArchievedFile(int idx, int archieveId) throws IOException {
		if (idx == 255 && archieveId == 255) {
			return cache.getReferenceTable();
		}
		RandomAccessFile indexFile = getIndexFile(idx);

		if(archieveId < 0 || archieveId >= (indexFile.length() * 6)) {
			System.out.println("error: ["+idx+", "+archieveId+"] ~ "+(indexFile.length() * 6));
			ByteBuffer fileBuffer = ByteBuffer.wrap(new byte[] {0, 0, 0, 0, 0});
			return new ArchiveFile(idx, archieveId, (ByteBuffer) fileBuffer);
		}

		ByteBuffer index = indexFile.getChannel().map(MapMode.READ_ONLY, 6 * archieveId, 6);

		int fileSize = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
		int currentSectorId = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);

		if (fileSize == 0 || currentSectorId == 0) {
			System.out.println("[Null] IDx: "+idx+" ArchieveID: "+archieveId);
			ByteBuffer fileBuffer = ByteBuffer.wrap(new byte[] {0, 0, 0, 0, 0});
			return new ArchiveFile(idx, archieveId, (ByteBuffer) fileBuffer);
		}

		int remainingBytes = fileSize;
		int cycles = 0;
		int blockLen = 512;
        int headerLen = 8;

		ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
		while(remainingBytes > 0) {
			int blockSize = remainingBytes > blockLen ? blockLen : remainingBytes;

			ByteBuffer block = dataFile.getChannel().map(MapMode.READ_ONLY, currentSectorId * 520, blockSize + headerLen);
			int sector_ArchieveId = block.getShort() & 0xffff;
			int currentPartId = block.getShort() & 0xffff;
			int nextSectorId = ((block.get() & 0xff) << 16) | ((block.get() & 0xff) << 8) | (block.get() & 0xff);
			int sector_IdxId = block.get() & 0xff;

			if(cycles != currentPartId) {
				throw new IOException("Cycle ["+currentPartId+" ~ "+cycles+"]");
			}

			if(remainingBytes > 0) {
				if(sector_IdxId != idx || sector_ArchieveId != archieveId) {
					throw new IOException("Invalid Sector ["+sector_IdxId+" ~ "+idx+"] ["+sector_ArchieveId+" ~ "+archieveId+"]");
				}
			}
			fileBuffer.put(block);
			
			remainingBytes -= blockSize;

			cycles++;
			currentSectorId = nextSectorId;
		}
		return new ArchiveFile(idx, archieveId, (ByteBuffer) fileBuffer.flip());
	}

	public RandomAccessFile getDataFile() {
		return dataFile;
	}

	public RandomAccessFile getIndexFile(int id) {
		if (id == 255) {
			return idx255IndexFile;
		}
		return indexFiles[id];
	}

	public void close() throws IOException {
		dataFile.close();
		for(RandomAccessFile indexFile : indexFiles) {
			indexFile.close();
		}
	}

	public RandomAccessFile getIdx255IndexFile() {
		return idx255IndexFile;
	}

}
