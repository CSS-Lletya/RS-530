package com.xeno.world;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.xeno.GameEngine;
import com.xeno.content.ClanManager;
import com.xeno.content.ShopManager;
import com.xeno.content.combat.Combat;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.item.GroundItemManager;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.AreaTask;
import com.xeno.entity.actor.player.task.CoordinateTask;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.entity.actor.player.task.TaskManager;
import com.xeno.entity.actor.player.task.impl.AreaVariables;
import com.xeno.entity.actor.player.task.impl.LevelChangeEvent;
import com.xeno.entity.actor.player.task.impl.LowerPotionCycles;
import com.xeno.entity.actor.player.task.impl.RestoreSpecialEvent;
import com.xeno.entity.actor.player.task.impl.RunEnergyEvent;
import com.xeno.entity.actor.player.task.impl.SkullCycleEvent;
import com.xeno.event.Event;
import com.xeno.net.Constants;
import com.xeno.net.entity.EntityList;
import com.xeno.net.entity.NPCUpdate;
import com.xeno.net.entity.PlayerUpdate;
import com.xeno.utility.Area;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.utility.XStreamUtil;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Represents the 'game world'.
 * @author Graham
 *
 */
public class World {
	
	/**
	 * The world instance.
	 */
	@Getter
	private static World instance = new World();
	
	/**
	 * Represents a valid Player check
	 */
	private final Predicate<Player> VALID_PLAYER = (player) -> player != null && !player.isDisconnected() && !player.isDestroyed();
	
	/**
	 * Represents a valid NPC check
	 */
	private final Predicate<NPC> VALID_NPC = (npc) -> npc != null && !npc.isDestroyed();
	
	/**
	 * Represents a valid Actor check
	 */
	public Stream<Actor> entities() {
		return Stream.concat(players(), npcs());
	}

	/**
	 * Gets a filtered stream of valid Players
	 * @return players
	 */
	public Stream<Player> players() {
		return players.stream().filter(VALID_PLAYER);
	}

	/**
	 * Gets a filtered stream of valid NPCS
	 * @return NPCS
	 */
	public Stream<NPC> npcs() {
		return npcs.stream().filter(VALID_NPC);
	}
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players;
	
	/**
	 * A list of npcs.
	 */
	private EntityList<NPC> npcs;
	
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
	}
	
	/**
	 * Register our global events.
	 */
	public void registerGlobalEvents() {
		World.getInstance().submit(new AreaVariables());
		World.getInstance().submit(new LevelChangeEvent());
		World.getInstance().submit(new LowerPotionCycles());
		World.getInstance().submit(new RestoreSpecialEvent());
		World.getInstance().submit(new RunEnergyEvent());
		World.getInstance().submit(new SkullCycleEvent());
	}
	
	/**
	 * Registers a 'coordiante' event.
	 * @param event
	 */
	public void registerCoordinateEvent(final CoordinateTask event) {
		submit(new Task(0) {
			
			@Override
			protected void execute() {
				boolean standingStill = event.getPlayer().getSprite().getPrimarySprite() == -1 && event.getPlayer().getSprite().getSecondarySprite() == -1;
				if(event.getPlayer().getDistanceEvent() == null || !event.getPlayer().getDistanceEvent().equals(event)) {
					this.stop();
					return;
				}
				if (standingStill) {
					if((event.getPlayer().getLocation().equals(event.getTargetLocation()) && event.getPlayer().getLocation().equals(event.getOldLocation())) || event.getFailedAttempts() >= 15) {
						if(this.getDelay() == 0) {
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
				setDelay(getDelay());
			}
		});
		submit(new Task(0) {
			@Override
			protected void execute() {
				boolean standingStill = event.getPlayer().getSprite().getPrimarySprite() == -1 && event.getPlayer().getSprite().getSecondarySprite() == -1;
				if(event.getPlayer().getDistanceEvent() == null || !event.getPlayer().getDistanceEvent().equals(event)) {
					this.stop();
					return;
				}
				if (standingStill) {
					if((event.getPlayer().getLocation().equals(event.getTargetLocation()) && event.getPlayer().getLocation().equals(event.getOldLocation())) || event.getFailedAttempts() >= 15) {
						if(this.getDelay() == 0) {
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
				this.setDelay(1);
			}
		});
	}
	
	public void registerCoordinateEvent(final AreaTask event) {
		submit(new Task(0) {
			
			@Override
			protected void execute() {
				boolean standingStill = event.getPlayer().getSprite().getPrimarySprite() == -1 && event.getPlayer().getSprite().getSecondarySprite() == -1;
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
				setDelay(1);
				stop();
			}
		});
	}
	
	/**
	 * Called whenever there is a major update.
	 */
	public void majorUpdate() {
		getTaskManager().sequence();
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
	 * Called every tick (600ms).
	 */
	public void tick() {
		players.stream().forEach(Player::processQueuedPackets);
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
		engine.getLoader().getWorkerThread().savePlayer(p);
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
	
	public void removeNPC(NPC npc) {
		for (NPC n : npcs) {
			if (n != null && npc == n) {
				n.setHidden(true);
				npcs.remove(n);
			}
		}
	}
	

	/**
	 * Sets the game engine.
	 * @param gameEngine
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@SneakyThrows(FileNotFoundException.class)
	public void setEngine(GameEngine gameEngine){
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
		return engine.getLoader().getMapData(region);
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

	/**
	 * The manager for the queue of game tasks.
	 */
	@Getter
	public final TaskManager taskManager = new TaskManager();
	
	/**
	 * Submits {@code t} to the backing {@link TaskManager}.
	 * @param task the task to submit to the queue.
	 */
	public void submit(Task task) {
		getTaskManager().submit(task);
	}
}