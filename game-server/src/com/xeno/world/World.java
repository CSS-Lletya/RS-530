package com.xeno.world;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.xeno.GameEngine;
import com.xeno.content.ClanManager;
import com.xeno.content.ShopManager;
import com.xeno.content.combat.AggressiveNPC;
import com.xeno.content.combat.Combat;
import com.xeno.entity.item.GroundItemManager;
import com.xeno.entity.npc.NPC;
import com.xeno.entity.player.Player;
import com.xeno.event.AreaEvent;
import com.xeno.event.CoordinateEvent;
import com.xeno.event.Event;
import com.xeno.event.impl.AreaVariables;
import com.xeno.event.impl.LevelChangeEvent;
import com.xeno.event.impl.LowerPotionCycles;
import com.xeno.event.impl.RunEnergyEvent;
import com.xeno.event.impl.SkullCycle;
import com.xeno.event.impl.SpecialRestore;
import com.xeno.net.Constants;
import com.xeno.net.entity.EntityList;
import com.xeno.packetbuilder.NPCUpdate;
import com.xeno.packetbuilder.PlayerUpdate;
import com.xeno.util.Area;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.util.XStreamUtil;

/**
 * Represents the 'game world'.
 * @author Graham
 *
 */
public class World {
	
	/**
	 * The world instance.
	 */
	private static World instance = null;
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players;
	
	/**
	 * A list of npcs.
	 */
	private EntityList<NPC> npcs;
	
	/**
	 * A list of pending events.
	 */
	private List<Event> events;
	private List<Event> eventsToAdd;
	private List<Event> eventsToRemove;
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * The item manager.
	 */
	private GroundItemManager itemManager;
	
	/**
	 * The global object manager.
	 */
	private ObjectManager objectManager;
	
	/**
	 * Manages pre-loaded object coordinates .
	 * (Rare objects that could be spawned client side, Rune rocks for example).
	 */
	private ObjectLocations objectLocations;
	
	/**
	 * The clan manager.
	 */
	private ClanManager clanManager;
	/**
	 * The shop manager.
	 */
	private ShopManager shopManager;

	
	private boolean updateInProgress;
	
	/**
	 * We create the world here.
	 */
	private World() {
		players = new EntityList<Player>(Constants.PLAYER_CAP);
		npcs = new EntityList<NPC>(Constants.NPC_CAP);
		events = new ArrayList<Event>();
		eventsToAdd = new ArrayList<Event>();
		eventsToRemove = new ArrayList<Event>();
	}
	
	/**
	 * Register our global events.
	 */
	public void registerGlobalEvents() {
		registerEvent(new RunEnergyEvent());
		registerEvent(new LevelChangeEvent());
		registerEvent(new SpecialRestore());
		registerEvent(new SkullCycle());
		registerEvent(new AreaVariables());
		registerEvent(new AggressiveNPC());
		registerEvent(new LowerPotionCycles());
	}
	
	/**
	 * Registers an event.
	 * @param event
	 */
	public void registerEvent(Event event) {
		eventsToAdd.add(event);
	}
	
	/**
	 * Registers a 'coordiante' event.
	 * @param event
	 */
	public void registerCoordinateEvent(final CoordinateEvent event) {
		registerEvent(new Event(0) {
			@Override
			public void execute() {
				boolean standingStill = event.getPlayer().getSprites().getPrimarySprite() == -1 && event.getPlayer().getSprites().getSecondarySprite() == -1;
				if(event.getPlayer().getDistanceEvent() == null || !event.getPlayer().getDistanceEvent().equals(event)) {
					this.stop();
					return;
				}
				if (standingStill) {
					if((event.getPlayer().getLocation().equals(event.getTargetLocation()) && event.getPlayer().getLocation().equals(event.getOldLocation())) || event.getFailedAttempts() >= 15) {
						if(this.getTick() == 0) {
							event.run();
							this.stop();
							event.setPlayerNull();
						} else {
							if(!event.hasReached()) {
								event.setReached(true);
							} else {
								event.run();
								this.stop();
								event.setPlayerNull();
							}
						}
					}
				} else {
					if(!event.getPlayer().getLocation().equals(event.getOldLocation())) {
						event.setOldLocation(event.getPlayer().getLocation());
					} else {
						event.incrementFailedAttempts();
					}
				}
				this.setTick(200);
			}
		});
	}
	
	public void registerCoordinateEvent(final AreaEvent event) {
		registerEvent(new Event(0) {
			@Override
			public void execute() {
				boolean standingStill = event.getPlayer().getSprites().getPrimarySprite() == -1 && event.getPlayer().getSprites().getSecondarySprite() == -1;
				if(event.getPlayer().getDistanceEvent() == null || !event.getPlayer().getDistanceEvent().equals(event)) {
					this.stop();
					return;
				}
				if (standingStill) {
					if(event.inArea()) {
						event.run();
						this.stop();
						event.setPlayerNull();
						return;
					}
				}
				this.setTick(500);
			}
		});
	}
	
	/**
	 * Processes any pending events.
	 */
	public void processEvents() {
		for(Event e : eventsToAdd) {
			events.add(e);
		}
		eventsToAdd.clear();
		for(Event e : events) {
			if(e.isStopped()) {
				eventsToRemove.add(e);
			} else if(e.isReady()) {
				e.run();
			}
		}
		for(Event e : eventsToRemove) {
			events.remove(e);
		}
		eventsToRemove.clear();
	}
	
	/**
	 * Get the world instance.
	 * @return
	 */
	public static World getInstance() {
		if(instance == null) {
			instance = new World();
		}
		return instance;
	}
	
	/**
	 * Called whenever there is a major update.
	 */
	public void majorUpdate() {
		for(Player p : players) {
			p.tick();
			p.processQueuedHits();
			p.getWalkingQueue().getNextPlayerMovement();
		}
		for(NPC n : npcs) {
			n.tick();
			n.processQueuedHits();
		}
		for(Player p : players) {
			// sometimes players aren't removed always: do that here
			if(!p.getPlayerDetails().getSession().isConnected()) {
				unregister(p);
			} else {
				PlayerUpdate.update(p);
				NPCUpdate.update(p);
			}
		}
		for(Player p : players) {
			p.getUpdateFlags().clear();
			p.getHits().clear();
			p.setRebuildNpcList(false);
		}
		for(NPC n : npcs) {
			n.getUpdateFlags().clear();
			n.getHits().clear();
		}
	}
	
	/**
	 * Called whenever there is a minor update.
	 */
	public void minorUpdate() {
		
	}
	
	/**
	 * Called every tick.
	 */
	public void tick() {
		for(Player p : players) {
			p.processQueuedPackets();
		}
		processEvents();
	}

	/**
	 * Gets the players list.
	 * @return
	 */
	public EntityList<Player> getPlayerList() {
		return players;
	}
	
	/**
	 * Gets the npcs list.
	 * @return
	 */
	public EntityList<NPC> getNpcList() {
		return npcs;
	}
	
	/**
	 * Register a player.
	 * @param p
	 * @return the player slot
	 */
	public int register(Player p) {
		int slot = -1;
		players.add(p);
		slot = p.getIndex();
		if(slot != -1) {
			LogUtility.log(LogType.INFO, "Registered " + p.getPlayerDetails().getDisplayName() + " [idx = "+slot+",online = "+players.size()+"]");
		} else {
			LogUtility.log(LogType.INFO, "Could not register " + p.getPlayerDetails().getDisplayName() + " - too many online [online = "+players.size()+"]");
		}
		return slot;
	}
	
	/**
	 * Unregister a player.
	 * @param p
	 */
	public void unregister(Player p) {
		if (p.getTrade() != null) {
			p.getTrade().decline();
			p.setTrade(null);
		}
		if (p.getTemporaryAttribute("cantDoAnything") != null && Area.inFightPits(p.getLocation())) {
		
			return;
		}
		
		if (!Combat.isXSecondsSinceCombat(p, p.getLastAttacked(), 10000) || p.isDead() || p.getTemporaryAttribute("unmovable") != null) {
			return;
		}
		removeAllPlayersNPCs(p);
		clanManager.leaveChannel(p);
		players.remove(p);
		engine.getWorkerThread().savePlayer(p);
		LogUtility.log(LogType.INFO, "Unregistered " + p.getPlayerDetails().getDisplayName() + " [online = "+players.size()+"]");
		p.getFriends().unregistered();
	}

	/**
	 * Will remove all NPCs which are spawned specifically for this player.
	 */
	public void removeAllPlayersNPCs(Player p) {
		for (NPC n : npcs) {
			if (n != null) {
				if (n.getOwner() != null) {
					if (n.getOwner().equals(p)) {
						n.setHidden(true);
						npcs.remove(n);
					}
				}
			}
		}
	}

	/**
	 * Sets the game engine.
	 * @param gameEngine
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void setEngine(GameEngine gameEngine) throws FileNotFoundException {
		this.engine = gameEngine;
		LogUtility.log(LogType.DEBUG, "Loading npcs spawns...");
		List<NPC> spawns = (List<NPC>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/npcs.xml"));
		for(NPC n : spawns) {
			npcs.add(n);
		}
		LogUtility.log(LogType.DEBUG, "Loaded " + spawns.size() + " npc spawns.");
		objectLocations = new ObjectLocations();
		shopManager = new ShopManager();
		itemManager = new GroundItemManager();
		objectManager = new ObjectManager();
		clanManager = new ClanManager();
		registerGlobalEvents();
		//cache = new LoadCache();
	}
	
	private final long serverStartupTime = System.currentTimeMillis();

	/**
	 * Gets mapdata for a region.
	 * @param region
	 * @return
	 */
	public int[] getMapData(int region) {
		return engine.getMapData(region);
	}
	
	/**
	 * Checks if a player is online.
	 * @param name
	 * @return
	 */
	public boolean isOnline(String name) {
		for(Player p : players) {
			if(p != null) {
				if(p.getPlayerDetails().getUsername().equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the item manager.
	 * @return
	 */
	public GroundItemManager getGroundItems() {
		return itemManager;
	}
	
	/**
	 * Gets the object manager.
	 * @return
	 */
	public ObjectManager getGlobalObjects() {
		return objectManager;
	}
	
	public ShopManager getShopManager() {
		return shopManager;
	}
	
	public ObjectLocations getObjectLocations() {
		return objectLocations;
	}

	public ClanManager getClanManager() {
		return clanManager;
	}

	public Player getPlayerForName(String name) {
		for (Player p : players) {
			if (p != null && p.getUsername().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public void setUpdateInProgress(boolean updateInProgress) {
		this.updateInProgress = updateInProgress;
	}

	public boolean isUpdateInProgress() {
		return updateInProgress;
	}

	public GameEngine getGameEngine() {
		return engine;
	}

	public long getServerStartupTime() {
		return serverStartupTime;
	}
}
