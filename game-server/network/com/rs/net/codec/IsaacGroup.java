package com.rs.net.codec;

import com.rs.net.codec.ISAACCipher;

/**
 * @author _Jordan <citellumrsps@gmail.com>
 */
public class IsaacGroup {

	/**
	 * Represents the isaac random for incoming packets.
	 */
	private final ISAACCipher incoming;

	/**
	 * Represents the isaac random for outgoing packets.
	 */
	private final ISAACCipher outgoing;

	/**
	 * Constructs a new {@code IsaacGroup} object.
	 *
	 * @param incoming The isaac to use for incoming packets.
	 * @param outgoing The isaac to use for outgoing packets.
	 */
	public IsaacGroup(ISAACCipher incoming, ISAACCipher outgoing) {
		this.incoming = incoming;
		this.outgoing = outgoing;
	}

	/**
	 * Gets the incoming.
	 * @return the incoming
	 */
	public ISAACCipher getIncoming() {
		return incoming;
	}

	/**
	 * Gets the outgoing.
	 * @return the outgoing
	 */
	public ISAACCipher getOutgoing() {
		return outgoing;
	}

}