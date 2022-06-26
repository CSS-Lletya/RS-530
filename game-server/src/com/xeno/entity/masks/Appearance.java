package com.xeno.entity.masks;

/**
 * Appearance class
 * @author Graham
 * @author Luke132
 *
 */
public class Appearance {
	
	private transient boolean invisible = false;
	private transient Appearance temporaryAppearance; // Used when designing character.
	private transient int walkAnimation;
	private boolean asNpc   = false;
	private int     npcId   = -1;
	private int     gender  = 0;
	private int[]   look    = new int[7];
	private int[]   colour  = new int[5];
	
	public Appearance() {
		look[1] = 10;
		look[2] = 18;
		look[3] = 26;
		look[4] = 33;
		look[5] = 36;
		look[6] = 42;
		walkAnimation = -1;
		for(int i = 0; i < 5; i++) {
			colour[i] = i*3+2;
		}
	}
	
	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setLook(int index, int look) {
		this.look[index] = look;
	}

	public void setColour(int index, int colour) {
		this.colour[index] = colour;
	}

	public boolean isNpc() {
		return asNpc;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public int getGender() {
		return gender;
	}
	
	public void setNpcId(int i) {
		npcId = i;
		asNpc = i != -1;
	}
	
	public int getLook(int id) {
		return look[id];
	}
	
	public int getColour(int id) {
		return colour[id];
	}
	
	public int[] getColoursArray() {
		return colour.clone();
	}
	
	public int[] getLookArray() {
		return look.clone();
	}

	public void setTemporaryAppearance(Appearance temporaryAppearance) {
		this.temporaryAppearance = temporaryAppearance;
	}

	public Appearance getTemporaryAppearance() {
		return temporaryAppearance;
	}

	public void setColoursArray(int[] colours) {
		this.colour = colours;
	}

	public void setLookArray(int[] look) {
		this.look = look;
	}

	public void setWalkAnimation(int walkAnimation) {
		this.walkAnimation = walkAnimation;
	}

	public int getWalkAnimation() {
		return walkAnimation;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isInvisible() {
		return invisible;
	}
}
