package com.xeno.model.masks;

/**
 * Represents 'still graphics'.
 * @author Graham
 *
 */
public class Graphics {
	
	private int id, delay, height;
	
	public Graphics(int id, int delay) {
		this.id = id;
		this.delay = delay;
		this.height = 0;
	}
	
	public Graphics(int id, int delay, int height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
	}
	
	public int getId() {
		return id;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getHeight() {
		return height;
	}
	
}
