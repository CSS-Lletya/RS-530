package com.xeno.content;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.xeno.entity.player.Player;
import com.xeno.event.AreaEvent;
import com.xeno.event.Event;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.util.Utility;
import com.xeno.util.XStreamUtil;
import com.xeno.world.Location;
import com.xeno.world.World;

public class DoorControl {

	private List<Door> doors = new ArrayList<Door>();
	private static final int PLAYER_CHANGE_DELAY = 1200;
	private static final int CHANGE_CYCLE_TIME = 180000;
	
	public DoorControl() {
		loadDoors();
		startCloseDoorEvent();
	}
	
	private void startCloseDoorEvent() {
		World.getInstance().registerEvent(new Event(CHANGE_CYCLE_TIME) {

			@Override
			public void execute() {
				for (Door door: doors) {
					if (door != null) {
						if (door.isDoorOpen() && !door.isInstantClose() && Utility.random(1) == 0) {
							changeDoor(door);
						}
					}
				}
			}
		});
	}
	
	public boolean useDoor(final Player p, int doorId, int doorX, int doorY, int doorHeight) {
		Location doorLocation = Location.location(doorX, doorY, doorHeight);
		for (Door door : doors) {
			int id = door.isDoorOpen() ? door.getOpenDoorId() : door.getClosedDoorId();
			if (id == doorId) {
				if (door.getDoorLocation().equals(doorLocation)) {
					if (door.isDoorOpen() && (System.currentTimeMillis() - door.getLastChangeTime() <= PLAYER_CHANGE_DELAY)) {
						// door was opened in the last PLAYER_CHANGE_DELAY ms..cant be instantly closed
						return true;
					} else if (!door.isClosable() && door.isDoorOpen()) {
						// door cannot be closed by a player
						return true;
					}
					final Door d = door;
					World.getInstance().registerCoordinateEvent(new AreaEvent(p, doorLocation.getX() - 1, doorLocation.getY() - 1, doorLocation.getX() + 1, doorLocation.getY() + 1) {

						@Override
						public void run() {
							changeDoor(p, d);
						}
					});
					return true;
				}
			}
		}
		return false;
	}
	
	protected void changeDoor(Door door) {
		updateDoorForPlayers(door);
	}

	protected void changeDoor(Player p, Door door) {
		p.setFaceLocation(door.getDoorLocation());
		updateDoorForPlayers(door);
	}
	
	private void updateDoorForPlayers(Door door) {
		Location loc = door.isDoorOpen() ? door.getClosedDoorLocation() : door.getOpenDoorLocation();
		int id = door.isDoorOpen() ? door.getClosedDoorId() : door.getOpenDoorId();
		int direction = door.isDoorOpen() ? door.getClosedDirection() : door.getOpenDirection();
		Location loc1 = door.isDoorOpen() ? door.getOpenDoorLocation() : door.getClosedDoorLocation();
		int direction1 = door.isDoorOpen() ? door.getOpenDirection() : door.getClosedDirection();
		for (Player p : World.getInstance().getPlayerList()) {
			if (p != null) {
				if (p.getLocation().withinDistance(door.getDoorLocation(), 60)) {
					p.getActionSender().removeObject(loc1, direction1, 0);
					p.getActionSender().createObject(id, loc, direction, 0);
				}
			}
		}
		door.setDoorOpen(!door.isDoorOpen());
		door.setLastChangeTime(System.currentTimeMillis());
	}
	
	public void refreshDoorsForPlayer(Player p) {
		for (Door door : doors) {
			if (door.getDoorLocation().withinDistance(p.getLocation(), 60)) {
				Location loc = door.isDoorOpen() ? door.getOpenDoorLocation() : door.getClosedDoorLocation();
				int id = door.isDoorOpen() ? door.getOpenDoorId() : door.getClosedDoorId();
				int direction = door.isDoorOpen() ? door.getOpenDirection() : door.getClosedDirection();
				Location loc1 = door.isDoorOpen() ? door.getClosedDoorLocation() : door.getOpenDoorLocation();
				int direction1 = door.isDoorOpen() ? door.getClosedDirection() : door.getOpenDirection();
				p.getActionSender().removeObject(loc1, direction1, 0);
				p.getActionSender().createObject(id, loc, direction, 0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadDoors() {
		try {
			List<Door> list = (List<Door>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/doors.xml"));
			for(Door list1 : list) {
				doors.add(list1);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*try {
			XStream xstream = XStreamUtil.getXStream();
			xstream.toXML(this, new FileOutputStream("data/doors.xml"));
		} catch(Exception e) {
		}*/
		LogUtility.log(LogType.INFO, "Loaded " + doors.size() + " door configurations.");
	}

	public static class Door {
		
		private static enum DefaultStatus {
			OPEN,
			CLOSED;
		}
		
		private DefaultStatus defaultStatus;
		private int openDoorId;
		private int closedDoorId;
		private Location closedDoorLocation;
		private Location openDoorLocation;
		private int openDirection;
		private int closedDirection;
		private boolean instantClose;
		private boolean closable;
		private boolean blockForOtherPlayers; // Used for things like warrior guild/quest doors
		private transient boolean doorOpen;
		private transient long lastChangeTime;
		
		public Door() {

		}
		
		public Object readResolve() {
			doorOpen = defaultStatus.equals(DefaultStatus.OPEN) ? true : false;
			return this;
		}

		public Location getOpenDoorLocation() {
			return openDoorLocation;
		}

		public boolean isInstantClose() {
			return instantClose;
		}

		public boolean isBlockForOtherPlayers() {
			return blockForOtherPlayers;
		}

		public int getOpenDoorId() {
			return openDoorId;
		}
		
		public int getClosedDoorId() {
			return closedDoorId;
		}

		public int getOpenDirection() {
			return openDirection;
		}

		public int getClosedDirection() {
			return closedDirection;
		}

		public void setDoorOpen(boolean doorOpen) {
			this.doorOpen = doorOpen;
		}

		public boolean isDoorOpen() {
			return doorOpen;
		}

		public void setClosedDoorLocation(Location closedDoorLocation) {
			this.closedDoorLocation = closedDoorLocation;
		}

		public Location getClosedDoorLocation() {
			return closedDoorLocation;
		}

		public Location getDoorLocation() {
			return doorOpen ? openDoorLocation : closedDoorLocation;
		}

		public void setLastChangeTime(long lastChangeTime) {
			this.lastChangeTime = lastChangeTime;
		}

		public long getLastChangeTime() {
			return lastChangeTime;
		}

		public boolean isClosable() {
			return closable;
		}

		public DefaultStatus getDefaultStatus() {
			return defaultStatus;
		}
	}
}
