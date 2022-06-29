package com.xeno.entity.actor.masks;

/**
 * Holds data for a single animation request.
 * @author Graham
 *
 */
public class Animation {
	
	private int id, delay;
	
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}
	
	public int getId() {
		return id;
	}
	
	public int getDelay() {
		return delay;
	}

}
