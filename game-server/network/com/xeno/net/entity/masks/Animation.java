package com.xeno.net.entity.masks;

import lombok.Data;

/**
 * Holds data for a single animation request.
 * @author Graham
 *
 */
@Data
public class Animation {
	
	private int id, delay;
	
	public Animation(int id) {
		this(id, 0);
	}
	
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}
	
	public Animation(int id, Runnable executable) {
		this(id, 0);
		executable.run();
	}
	
	public Animation(int id, int delay, Runnable executable) {
		this.id = id;
		this.delay = delay;
		executable.run();
	}
}