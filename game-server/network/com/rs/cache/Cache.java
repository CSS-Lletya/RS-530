package com.rs.cache;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * 
 *
 * @author Flamable
 */
public class Cache {

	@SuppressWarnings("unused")
	private static Cache cache;
	private final FileInfomationTable[] fileInfomationTable;
	private final FileSystem fileSystem;
	private ArchiveFile referenceTable;
	
	/**
	 * 
	 * @param directory
	 * @throws Exception
	 */
	public Cache(File directory) throws Exception {
		System.out.println("Loading Cache.... "+directory.toString());
		if (!directory.isDirectory() || !directory.exists()) {
			throw new Exception("Invalid Cache path");
		}

		final RandomAccessFile dataFile;
		final RandomAccessFile[] indexFiles;
		final RandomAccessFile idx255IndexFile;

		try {
			/**
			 * Load Reference Table
			 */
			idx255IndexFile = new RandomAccessFile(directory.getAbsolutePath() + "/main_file_cache.idx255", "r");
			final int idx_Count = (int) (idx255IndexFile.length() / 6);


			indexFiles = new RandomAccessFile[idx_Count];
			fileInfomationTable = new FileInfomationTable[idx_Count];

			/**
			 * Load and cache the Dat file
			 */
			dataFile = new RandomAccessFile(directory.getAbsolutePath() + "/main_file_cache.dat2", "r");

			/**
			 * Load and cache the indexFiles
			 */
			for(int i = 0; i < indexFiles.length; i++) {
				indexFiles[i] = new RandomAccessFile(directory.getAbsolutePath() + "/main_file_cache.idx" + i, "r");
			}

			/**
			 * Setup File System
			 */
			fileSystem = new FileSystem(this, dataFile, indexFiles, idx255IndexFile);

			/**
			 * Setup FIT
			 */
			for (int i = 0; i < indexFiles.length; i++) {
				fileInfomationTable[i] = new FileInfomationTable(this, i);
			}
		} catch (Exception e) {
			throw new Exception("Error dueing cache loading");
		} finally {
			System.out.println("Cache loaded");
			System.out.println("Generating ReferenceTable...");
			referenceTable = new ArchiveFile(255, 255, (ByteBuffer) generateRefTable());
			System.out.println("ReferenceTable Generated");
		}
	}

	public ByteBuffer generateRefTable() {
		try {
			int indexCount = (int) (fileSystem.getIdx255IndexFile().length() / 6);
			CRC32 crc = new CRC32();
			boolean containsRevision = true; // TODO check
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream checksum = new DataOutputStream(stream);
			checksum.write(0);
			checksum.writeInt(indexCount * (containsRevision ? 8 : 4));
			for(int i=0; i < indexCount; i++) {
				ArchiveFile af = fileSystem.getArchievedFile(255, i);
				ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
				DataOutputStream header = new DataOutputStream(stream2);
				header.write(af.getCompression());
				header.writeInt(af.getUncompressedSize());
				header.flush();
				crc.update(af.getBytes());
				checksum.writeInt((int) crc.getValue());
				if(containsRevision)
					checksum.writeInt(containsRevision ? getFileInfomationTable()[i].fit_revision : 0);
				header.close();
				crc.reset();
			}
			checksum.flush();
			byte[] cacheChecksum = stream.toByteArray();
			stream.close();
			return ByteBuffer.wrap(cacheChecksum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public ArchiveFile getReferenceTable() {
		return referenceTable;
	}

	public FileInfomationTable[] getFileInfomationTable() {
		return fileInfomationTable;
	}

}
