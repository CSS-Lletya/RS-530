package com.xeno.net.codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.xeno.net.WorkerThread;

/**
 * Provides access to the encoders and decoders for the 508 protocol.
 * @author Graham
 *
 */
public class CodecFactory implements ProtocolCodecFactory {

	/**
	 * The encoder.
	 */
	private ProtocolEncoder encoder;

	/**
	 * The decoder.
	 */
	private ProtocolDecoder decoder;

	/**
	 * Constructs a new {@code CodecFactory}.
	 * @param workerThread
	 */
	public CodecFactory(WorkerThread workerThread) {
		this.decoder = new RS2LoginProtocolDecoder(workerThread);
		this.encoder = new RS2ProtocolEncoder(null);
	}

	/**
	 * Constructs a new {@code CodecFactory}.
	 * @param group
	 */
	public CodecFactory(IsaacGroup group) {
		this.decoder = new RS2ProtocolDecoder(group.getIncoming());
		this.encoder = new RS2ProtocolEncoder(group.getOutgoing());
	}

	@Override
	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

}