package com.rs.net.codec.js5;

/**
 * 
 *
 * @author Flamable
 */
public class Js5FileRequest {

	private final int idxId;
	private final int fileId;
	private final int priority;
	
	public Js5FileRequest(int idxId, int fileId, int pri) {
		this.idxId = idxId;
		this.fileId = fileId;
		this.priority = pri;
	}

	public int getIdxId() {
		return idxId;
	}

	public int getFileId() {
		return fileId;
	}

	public int getPriority() {
		return priority;
	}

}
