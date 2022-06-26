package com.xeno.content;

import java.util.ArrayList;
import java.util.List;

import com.xeno.entity.player.Player;
import com.xeno.event.AreaEvent;
import com.xeno.event.CoordinateEvent;
import com.xeno.event.Event;
import com.xeno.model.player.skills.magic.Teleport;
import com.xeno.util.Lever;
import com.xeno.world.Location;
import com.xeno.world.World;

public class LaddersAndStairs {

	private static final int COORDINATE_POSITION = 0;
	private static final int AREA_POSITION = 1;
	
	private static LaddersAndStairs instance = null;
	private List<HeightObject> objects = new ArrayList<HeightObject>();
	private List<Lever> levers = new ArrayList<Lever>();
	
	public LaddersAndStairs() {
		loadObjects();
	}
	
	public static LaddersAndStairs getInstance() {
		if (instance == null) {
			instance =  new LaddersAndStairs();
		}
		return instance;
	}
	
	public boolean useObject(final Player p, int id, Location location, int option) {
		for (HeightObject object : objects) {
			if (object.getId() == id) {
				if (object.getLocation().equals(location) && object.getOption() == option) {
					final HeightObject obj = object;
					if (object.getType() == COORDINATE_POSITION) {
						World.getInstance().registerCoordinateEvent(new CoordinateEvent(p, object.getStandLocation()) {
							
							@Override
							public void run() {
								teleport(p, obj);
							}
						});
					} else if (object.getType() == AREA_POSITION) {
						World.getInstance().registerCoordinateEvent(new AreaEvent(p, object.getMinCoords().getX(), object.getMinCoords().getY(), object.getMaxCoords().getX(), object.getMaxCoords().getY()) {
							
							@Override
							public void run() {
								teleport(p, obj);
							}
						});
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void teleport(final Player p, final HeightObject obj) {
		p.getWalkingQueue().reset();
		p.setTemporaryAttribute("unmovable", true);
		p.setFaceLocation(obj.getLocation());
		if (obj.getAnimation() != -1) {
			p.animate(obj.getAnimation());
		}
		World.getInstance().registerEvent(new Event(obj.getAnimation() != -1 ? obj.getTeleDelay() : 500) {

			@Override
			public void execute() {
				this.stop();
				p.teleport(obj.getTeleLocation());
				p.removeTemporaryAttribute("unmovable");
			}
		});
	}
	
	public void useLever(final Player p, final int id, Location leverLocation) {
		if (p.getTemporaryAttribute("teleporting") != null) {
			return;
		}
		for (Lever lever : levers) {
			if (lever.getId() == id) {
				if (lever.getLeverLocation().equals(leverLocation)) {
					final Lever l = lever;
					//TODO when in use it cant be used (in use = lever is facing down)
					World.getInstance().registerCoordinateEvent(new CoordinateEvent(p, l.getLeverLocation()) {

						@Override
						public void run() {
							p.setFaceLocation(l.getFaceLocation());
							if (p.getTemporaryAttribute("teleblocked") != null) {
								p.getActionSender().sendMessage("A magical force prevents you from teleporting!");
								return;
							} else if ((p.getTemporaryAttribute("teleporting") != null )) {
								return;
							}
							p.animate(2140);
							p.getActionSender().closeInterfaces();
							p.setTemporaryAttribute("teleporting", true);
							p.getWalkingQueue().reset();
							p.getActionSender().clearMapFlag();
							l.setInUse(true);
							World.getInstance().registerEvent(new Event(700) {

								@Override
								public void execute() {
									this.stop();
									p.animate(8939, 0);
									p.graphics(1576, 0);
									l.setInUse(false);
									World.getInstance().registerEvent(new Event(1800) {

										@Override
										public void execute() {
											this.stop();
											p.teleport(l.getTeleLocation());
											p.animate(8941, 0);
											p.graphics(1577, 0);
											Teleport.resetTeleport(p);
										}
									});
								}
							});
						}
					});
					break;
				}
			}
		}
	}

	private void loadObjects() {
		loadLevers();
		HeightObject ho = new HeightObject(24354, Location.location(3214, 3410, 0), Location.location(3214, 3411, 0), Location.location(3214, 3411, 1), 828);
		HeightObject h1 = new HeightObject(24355, Location.location(3214, 3410, 1), Location.location(3214, 3411, 1), Location.location(3214, 3411, 0), 827);
		HeightObject h2 = new HeightObject(24365, Location.location(3187, 9833, 0), Location.location(3188, 3432, 0), Location.location(3190, 9833, 0), Location.location(3190, 9834, 0));
		HeightObject h3 = new HeightObject(24360, Location.location(3189, 3432, 0), Location.location(3190, 9834, 0), Location.location(3188, 3432, 0), Location.location(3188, 3433, 0));
		objects.add(ho);
		objects.add(h1);
		objects.add(h2);
		objects.add(h3);
	}

	private void loadLevers() {
		Lever mageBankLeverOutside = new Lever(5959, Location.location(3090, 3956, 0), Location.location(3089, 3956, 0), Location.location(2539, 4712, 0));
		Lever mageBankLeverInside = new Lever(5960, Location.location(2539, 4712, 0), Location.location(2539, 4711, 0), Location.location(3090, 3956, 0));
		levers.add(mageBankLeverOutside);
		levers.add(mageBankLeverInside);
	}

	public static class HeightObject {
		

		private int id = -1;
		private int type = 0;
		private int option = 1;
		private Location location = null;
		private Location teleLocation = null;
		private Location standLocation = null;
		private Location minCoords = null;
		private Location maxCoords = null;
		private int teleDelay = 1000;
		private int animation = -1;
		
		public HeightObject(int id, Location loc, Location standLoc, Location teleLoc, int anim) {
			this.id = id;
			this.location = loc;
			this.teleLocation = teleLoc;
			this.animation = anim;
			this.standLocation = standLoc;
			this.type = LaddersAndStairs.COORDINATE_POSITION;
		}
		
		public HeightObject(int id, Location loc, Location teleLoc, Location minCoords, Location maxCoords, int anim) {
			this.id = id;
			this.location = loc;
			this.teleLocation = teleLoc;
			this.animation = anim;
			this.minCoords = minCoords;
			this.maxCoords = maxCoords;
			this.type = LaddersAndStairs.AREA_POSITION;
		}
		
		public HeightObject(int id, Location loc, Location teleLoc, Location minCoords, Location maxCoords) {
			this.id = id;
			this.location = loc;
			this.teleLocation = teleLoc;
			this.minCoords = minCoords;
			this.maxCoords = maxCoords;
			this.type = LaddersAndStairs.AREA_POSITION;
		}
		
		public int getId() {
			return id;
		}

		public Location getLocation() {
			return location;
		}

		public Location getTeleLocation() {
			return teleLocation;
		}

		public int getTeleDelay() {
			if (animation == 828) {
				return 1000;
			}
			return teleDelay;
		}

		public int getAnimation() {
			return animation;
		}
		
		public int getType() {
			return type;
		}

		public Location getStandLocation() {
			return standLocation;
		}

		public Location getMinCoords() {
			return minCoords;
		}

		public Location getMaxCoords() {
			return maxCoords;
		}
		
		public int getOption() {
			return option;
		}
	}
}
