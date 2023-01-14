package com.rs.net.codec.js5;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 
 *
 * @author Flamable
 */
public class Js5CodecFactory implements ProtocolCodecFactory {
	
	public Js5CodecFactory() {
	}
	
	/**
	 * The encoder.
	 */
	private ProtocolEncoder encoder = new Js5Encoder();
	
	/**
	 * The decoder.
	 */
	private ProtocolDecoder decoder = new Js5Decoder();
	
	@Override
	/**
	 * Get the encoder.
	 */
	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

	@Override
	/**
	 * Get the decoder.
	 */
	public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

}
