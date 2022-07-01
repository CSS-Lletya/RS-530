package com.xeno.entity.actor.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;

import com.xeno.content.Clan;
import com.xeno.content.ShopSession;
import com.xeno.content.TradeSession;
import com.xeno.content.emote.SkillCapes;
import com.xeno.entity.Entity;
import com.xeno.entity.EntityType;
import com.xeno.entity.Follow;
import com.xeno.entity.Location;
import com.xeno.entity.WalkingQueue;
import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.item.GroundItem;
import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.model.player.skills.Skills;
import com.xeno.model.player.skills.prayer.PrayerData;
import com.xeno.model.player.skills.prayer.Prayers;
import com.xeno.model.player.skills.prayer.ProtectedItems;
import com.xeno.net.ActionSender;
import com.xeno.net.Packet;
import com.xeno.net.definitions.NPCDefinition;
import com.xeno.net.entity.LocalEntityList;
import com.xeno.net.entity.PlayerUpdateFlags;
import com.xeno.net.entity.masks.Animation;
import com.xeno.net.entity.masks.Appearance;
import com.xeno.net.entity.masks.ChatMessage;
import com.xeno.net.entity.masks.EntityFocus;
import com.xeno.net.entity.masks.FaceLocation;
import com.xeno.net.entity.masks.ForceMovement;
import com.xeno.net.entity.masks.ForceText;
import com.xeno.net.entity.masks.Graphics;
import com.xeno.net.entity.masks.Hits;
import com.xeno.net.entity.masks.Hits.Hit;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.packetbuilder.packets.OutgoingPacketDispatcher;
import com.xeno.utility.RandomUtils;
import com.xeno.world.World;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a connected player.
 * @author Graham
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Player extends Actor {
	
	/**
	 * Represents a Players credentials.
	 */
	private PlayerCredentials playerCredentials;
	
	/**
	 * Represents a Players personal details & attributes obtained overtime.
	 */
	public PlayerDetails playerDetails;
	
	/**
	 * Represents a Players incoming packets & utility methods.
	 */
	private transient ActionSender actionSender;
	
	/**
	 * Represents a collection of queued Packets.
	 */
	private transient ObjectArrayFIFOQueue<Packet> queuedPackets;
	
	/**
	 * Represents a Players Update Flags & respective conditions.
	 */
	private transient PlayerUpdateFlags updateFlags;
	
	/**
	 * Represents a Players Trading Session.
	 */
	private transient TradeSession trade;
	
	/**
	 * Represents a Players current Walking queue.
	 */
	private transient WalkingQueue walkingQueue;
	private transient LocalEntityList localEntities;
	private transient ChatMessage lastChatMessage;
	private transient Animation lastAnimation;
	private transient Graphics lastGraphics;
	private transient EntityFocus entityFocus;
	private transient ForceText forceText;
	private transient FaceLocation faceLocation;
	private transient ForceMovement forceMovement;
	private transient ShopSession shopSession;
	private transient Clan clan;
	private transient Prayers prayers;
	private transient SkillCapes skillCapes;
	private Bank bank;
	private Appearance appearance;
	private Equipment equipment;
	private Skills skills;
	private Inventory inventory;
	private Friends friends;
    
    public transient InterfaceManager interfaceManager;
	private transient Queue<Hit> queuedHits;
	private transient List<Player> tradeRequests;
	private transient List<Player> duelRequests;
	private transient int lastWildLevel;
	private transient boolean hd;
	private transient Object distanceEvent;
	
	public Player(PlayerCredentials details) {
		this.playerCredentials = details;
		this.appearance = new Appearance();
		this.equipment = new Equipment();
		this.skills = new Skills();
		this.inventory = new Inventory();
		this.friends = new Friends();
		this.playerDetails = new PlayerDetails();
		this.bank = new Bank();
		this.playerDetails.setDefaultSettings();
	}

	/**
	 * Called when XStream loads us.
	 * 
	 * NOTE: other loaders should call this also.
	 */
	public Object readResolve() {
		super.readResolve(EntityType.PLAYER);
		actionSender = new ActionSender(this);
		follow = new Follow(this);
		queuedPackets = new ObjectArrayFIFOQueue<Packet>();
		updateFlags = new PlayerUpdateFlags();
		walkingQueue = new WalkingQueue(this);
		skills.setPlayer(this);
		inventory.setPlayer(this);
		bank.setPlayer(this);
		equipment.setPlayer(this);
		friends.setPlayer(this);
		localEntities = new LocalEntityList();
		playerDetails.setPlayer(this);
		prayers = new Prayers(this);
		lastChatMessage = null;
		lastAnimation = null;
		lastGraphics = null;
		entityFocus = null;
		forceText = null;
		faceLocation = null;
		forceMovement = null;
		tradeRequests = new ArrayList<Player>();
		duelRequests = new ArrayList<Player>();
		clan = null;
		lastWildLevel = -1;
		playerCredentials.refreshLongName();
		queuedHits = new LinkedList<Hit>();
		hd = false;
		skillCapes = new SkillCapes(this);
		interfaceManager = new InterfaceManager(this);
		return this;
	}

	/**
	 * Called roughly every 600ms.
	 */
	
	public void tick() {
//		if (this.inCombat()) {
//			Combat.combatLoop(this);
//		}
		if (getFollow().getFollowing() != null) {
			getFollow().followEntity();
		}
	}
	
	public IoSession getSession() {
		return this.playerCredentials.getSession();
	}
	
	public String getUsername() {
		return this.playerCredentials.getDisplayName();
	}
	
	public String getBaseUsername() {
		return this.playerCredentials.getUsername();
	}
	
	public void processQueuedPackets() {
		synchronized(queuedPackets) {
			ObjectArrayFIFOQueue<Packet> packet = null;
			while(!queuedPackets.isEmpty() && (packet = queuedPackets) != null) {
				OutgoingPacketDispatcher.execute(this, packet.first(), packet.first().getId());
				packet.dequeue();
			}
		}
	}
	
	public void addPacketToQueue(Packet packet) {
		synchronized(queuedPackets) {
			queuedPackets.enqueue(packet);
		}
	}

	public boolean isDisconnected() {
		return !getSession().isConnected();
	}

	public void setPlayerListSize(int playerListSize) {
		localEntities.playerListSize = playerListSize;
	}

	public int getPlayerListSize() {
		return localEntities.playerListSize;
	}

	public Player[] getPlayerList() {
		return localEntities.playerList;
	}
	public byte[] getPlayersInList() {
		return localEntities.playersInList;
	}
	
	public void setNpcListSize(int npcListSize) {
		localEntities.npcListSize = npcListSize;
	}

	public int getNpcListSize() {
		return localEntities.npcListSize;
	}

	public void setNpcList(NPC[] npcList) {
		localEntities.npcList = npcList;
	}

	public NPC[] getNpcList() {
		return localEntities.npcList;
	}
	
	public byte[] getNpcsInList() {
		return localEntities.npcsInList;
	}
	
	public void setEntityFocus(EntityFocus entityFocus) {
		this.entityFocus = entityFocus;
		updateFlags.setEntityFocusUpdateRequired(true);
	}

	@Override
	public EntityFocus getEntityFocus() {
		return entityFocus;
	}
	
	public void setForceText(ForceText forceText) {
		this.forceText = forceText;
		updateFlags.setForceTextUpdateRequired(true);
	}
	
	public void setForceMovement(ForceMovement movement) {
		this.forceMovement = movement;
		updateFlags.setForceMovementRequired(true);
	}
	
	public void setFaceLocation(FaceLocation faceLocation) {
		this.faceLocation = faceLocation;
		updateFlags.setFaceLocationUpdateRequired(true);
	}

	public void setRebuildNpcList(boolean b) {
		this.localEntities.rebuildNpcList = b;
	}
	
	public void processQueuedHits() {
		if(!updateFlags.isHitUpdateRequired()) {
			if(queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				hit(h.getDamage(), h.getType());
			}
		}
		if(!updateFlags.isHit2UpdateRequired()) {
			if(queuedHits.size() > 0) {
				Hit h = queuedHits.poll();
				hit(h.getDamage(), h.getType());
			}
		}
	}
	
	public void forceChat(String message) {
		setForceText(new ForceText(message));
		updateFlags.setForceTextUpdateRequired(true);
	}
	
	public void hit(int damage) {
		hit(damage, damage == 0 ? Hits.HitType.NO_DAMAGE : Hits.HitType.NORMAL_DAMAGE);
	}
	
	public void hit(int damage, Hits.HitType type) {
		if (isDead()) {
			damage = 0;
			type = Hits.HitType.NO_DAMAGE;
		}
		boolean redemption = prayers.getHeadIcon() == PrayerData.REDEMPTION;
		byte maxHp = (byte) getMaxHp();
		byte newHp = (byte) (getHp() - damage);
		if (redemption) {
			if (newHp >= 1 && newHp <= maxHp * 0.10) {
				setNextGraphic(new Graphics(436, 0, 0));
				actionSender.sendMessage("Using your prayer skill, you heal yourself.");
				skills.setLevel(5, 0);
				actionSender.sendSkillLevel(5);
				heal((int) (skills.getLevelForXp(5) * 0.25));
			}
		}
			if (newHp >= 1 && newHp <= maxHp * 0.10 && !redemption) {
				if (equipment.getItemInSlot(12) == 2570) {
					teleport(Location.location(3221 + RandomUtils.random(1), 3217 + RandomUtils.random(3), 0));
					actionSender.sendMessage("Your ring of life shatters whilst teleporting you to safety.");
					equipment.getSlot(12).setItemId(-1);
					equipment.getSlot(12).setItemAmount(0);
					actionSender.refreshEquipment();
					queuedHits.clear();
//					Combat.resetCombat(this, 1);
					return;
				}
			}
		boolean damageOverZero = damage > 0;
		if(damage > skills.getLevel(3)) {
			damage = skills.getLevel(3);
		}
		if (damageOverZero && damage == 0) {
			type = Hits.HitType.NO_DAMAGE;
		}
		if(!updateFlags.isHitUpdateRequired()) {
			getHits().setHit1(new Hit(damage, type));
			updateFlags.setHitUpdateRequired(true);
		} else {
			if(!updateFlags.isHit2UpdateRequired()) {
				getHits().setHit2(new Hit(damage, type));
				updateFlags.setHit2UpdateRequired(true);
			} else {
				queuedHits.add(new Hit(damage, type));
				return;
			}
		}
		skills.setLevel(3, skills.getLevel(3) - damage);
		if(skills.getLevel(3) <= 0) {
			skills.setLevel(3, 0);
			if(!isDead()) {
//				World.getInstance().registerEvent(new DeathEvent(this));//TODO: Finish converting
				setDead(true);
			}
		}
		actionSender.sendSkillLevel(3);
	}

	public void setHd(boolean b) {
		this.hd = b;
		playerCredentials.setHd(b);
	}
	
	public boolean isHd() {
		return playerCredentials.isHd();
	}
	
	public void setAppearance(Appearance newAppearance) {
		this.appearance = newAppearance;
		updateFlags.setAppearanceUpdateRequired(true);
	}

	public boolean wantsToTrade(Player p2) {
		for (Player p : tradeRequests) {
			if (p != null) {
				if (p.equals(p2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void newTradeRequest(Player p2) {
		if (wantsToTrade(p2)) {
			return;
		}
		tradeRequests.add(p2);
	}
	
	public boolean wantsToDuel(Player p2) {
		for (Player p : duelRequests) {
			if (p != null) {
				if (p.equals(p2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void newDuelRequest(Player p2) {
		if (wantsToDuel(p2)) {
			return;
		}
		duelRequests.add(p2);
	}
	

	@Override
	public void dropLoot() {
		Entity killer = this.getKiller();
		Player klr = killer instanceof NPC ? null : (Player) killer;
		if (klr == null) {
			klr = this;
		}
		int amountToKeep = playerDetails.isSkulled() ? 0 : 3;
		if (prayers.isProtectItem()) {
			amountToKeep = playerDetails.isSkulled() ? 1 : 4;
		}
		int[] protectedItems = new int[amountToKeep];
		boolean[] saved = new boolean[amountToKeep];
		if (protectedItems.length > 0) {
			protectedItems[0] = ProtectedItems.getProtectedItem1(this)[0];
		} 
		if (protectedItems.length > 1) {
			protectedItems[1] = ProtectedItems.getProtectedItem2(this)[0];
		}
		if (protectedItems.length > 2) {
			protectedItems[2] = ProtectedItems.getProtectedItem3(this)[0];
		}
		if (protectedItems.length > 3) {
			protectedItems[3] = ProtectedItems.getProtectedItem4(this)[0];
		}
		boolean save = false;
		for(int i = 0; i < 28; i++) {
			save = false;
			Item item = inventory.getSlot(i);
			if (item.getItemId() > 0) {
				for (int j = 0; j < protectedItems.length; j++) {
					if (amountToKeep > 0 && protectedItems[j] > 0) {
						if (!saved[j] && !save) {
							if (item.getItemId() == protectedItems[j] && item.getItemAmount() == 1) {
								saved[j] = true;
								save = true;
							}
							if (item.getItemId() == protectedItems[j] && item.getItemAmount() > 1) {
								item.setItemAmount(item.getItemAmount() - 1);
								saved[j] = true;
								save = true;
							}
						}
					}
				}
				if (!save) {
					int itemId = item.getItemId();
					GroundItem gi = new GroundItem(itemId, item.getItemAmount(), this.getLocation(), item.getDefinition().isPlayerBound() ? this : klr);
					World.getInstance().getGroundItems().newEntityDrop(gi);
				}
			}
		}
		inventory.clearAll();
		saved = new boolean[amountToKeep];
		for(int i = 0; i < 14; i++) {
			save = false;
			Item item = this.getEquipment().getSlot(i);
			if (item.getItemId() > 0) {
				for (int j = 0; j < protectedItems.length; j++) {
					if (amountToKeep > 0 && protectedItems[j] > -1) {
						if (!saved[j] && !save) {
							if (item.getItemId() == protectedItems[j] && item.getItemAmount() == 1) {
								saved[j] = true;
								save = true;
							}
							if (item.getItemId() == protectedItems[j] && item.getItemAmount() > 1) {
								item.setItemAmount(item.getItemAmount() - 1);
								saved[j] = true;
								save = true;
							}
						}
					}
				}
				if (!save) {
					int itemId = item.getItemId();
					GroundItem gi = new GroundItem(itemId, item.getItemAmount(), this.getLocation(), item.getDefinition().isPlayerBound() ? this : klr);
					World.getInstance().getGroundItems().newEntityDrop(gi);
				}
			}
		}
		equipment.clearAll();
		GroundItem gi = new GroundItem(526, 1, this.getLocation(), klr);
		World.getInstance().getGroundItems().newEntityDrop(gi);
		inventory.setProtectedItems(protectedItems);
	}

	@Override
	public int getAttackAnimation() {
//		return !this.appearance.isNpc() ? Animations.getAttackAnimation(this) : NPCDefinition.forId(this.appearance.getNpcId()).getAttackAnimation();
		return 0;
	}

	@Override
	public int getAttackSpeed() {
//		if (getMiasmicEffect() > 0) {
//			return Animations.getAttackSpeed(this) * 2;
//		}
//		return Animations.getAttackSpeed(this);
		return 0;
	}

	@Override
	public int getDeathAnimation() {
		return !this.appearance.isNpc() ? 7185 : NPCDefinition.forId(this.appearance.getNpcId()).getDeathAnimation();
	}

	@Override
	public int getDefenceAnimation() {
//		return !this.appearance.isNpc() ? Animations.getDefenceAnimation(this) : NPCDefinition.forId(this.appearance.getNpcId()).getDefenceAnimation();
		return 0;
	}

	@Override
	public int getHitDelay() {
//		return Animations.getPlayerHitDelay(this);
		return 0;
	}

	@Override
	public int getHp() {
		return this.getSkills().getLevel(3);
	}

	@Override
	public int getMaxHit() {
//        // float effectiveStr = (float) Math.floor((skills.getLevel(2)) * 1/*prayer*/ * 1/*other*/ + 0/*style*/);
//        /*float baseDmg = 1.3F + (effectiveStr / 9.5F) + (bonuses.getBonus(11) / 8F) + ((effectiveStr * bonuses.getBonus(11)) / 64F);
//        return (int) baseDmg * bonuses.getBonus(11);*/
//		int a = skills.getLevel(2);
//		int b = bonuses.getBonus(11);
//		CombatSkill fightType = this.getAttackVars().getSkill();
//		double c = (double)a;
//		double d = (double)b;
//		double e = 0;
//		double f = 0;
//		double g = 0;
//		double gg = 0;
//		double h = 0;
//		int strPrayer = prayers.getStrengthPrayer();
//		if (strPrayer == 1) {
//			gg = 0.05;
//		} else if (strPrayer == 2) {
//			gg = 0.1;
//		} else if (strPrayer == 3) {
//			gg = 0.15;
//		} else if (prayers.getSuperPrayer() == 1) {
//			gg = 0.18;
//		} else if (prayers.getSuperPrayer() == 2) {
//			gg = 0.23;
//		}
//		e = c*(1+g+gg);
//		if(fightType.equals(CombatSkill.AGGRESSIVE)) {
//			e += 3;
//		}
//		if(fightType.equals(CombatSkill.CONTROLLED)) {
//			e += 1;
//		}
//		f = (d*0.00175)+0.1;
//		h = Math.floor(e*f);//2.05);
//		if (a >= 80) {
//			h -= 2.0;
//		} else {
//			h += 2.0;
//		}
////		if (CombatFormula.wearingMeleeVoid(this)) {
////			h *= 1.10;
////		}
//		return (int) h;
//
//		/*double maxHit = 0;
//		if(this.getCombatType().equals(CombatType.MELEE)) {
//			int strBonus = bonuses.getBonus(11);
//			int strength = skills.getLevel(2);
//			int fightType = this.getSettings().getAttackStyle();
//			if(fightType == 1 || fightType == 4) {
//				maxHit += (double) (1.05 + (double) ((double) (strBonus * strength) * 0.00175));
//			} else if(fightType == 2) {
//				maxHit += (double) (1.35 + (double) ((double) (strBonus) * 0.00525));
//			} else if(fightType == 3){
//				maxHit += (double) (1.15 + (double) ((double) (strBonus) * 0.00175));
//			}
//			int strPrayer = prayers.getStrengthPrayer();
//			if (strPrayer == 1) {
//				maxHit += (double) 0.05;
//			} else if (strPrayer == 2) {
//				maxHit += (double) 0.1;
//			} else if (strPrayer == 3) {
//				maxHit += (double) 0.15;
//			} else if (prayers.getSuperPrayer() == 1) {
//				maxHit += (double) 0.18;
//			} else if (prayers.getSuperPrayer() == 2) {
//				maxHit += (double) 0.23;
//			}
//			maxHit += (double)(strength * 0.1);
//			System.out.println("maxHit " + maxHit);
//		} else if(this.getCombatType().equals(CombatType.RANGE)) {
//			int range = skills.getLevel(4);
//			int rangeBonus = bonuses.getBonus(4);
//			double d = ((double) (rangeBonus * 0.00175D) + 0.1D);
//			maxHit += d * (double) range + 2.05D;
//		} else {
//			// Magic spells have a set max hit.
//		}
//		return ((int) (Math.ceil(maxHit)) + 1);*/
		return 0;
	}

	@Override
	public int getMaxHp() {
		return this.getSkills().getLevelForXp(3);
	}

	@Override
	public void heal(int amount) {
		if (isDead()) {
			return;
		}
		if ((skills.getLevel(3) + amount) > (skills.getLevelForXp(3))) {
			skills.setLevel(3, skills.getLevelForXp(3));
			actionSender.sendSkillLevel(3);
			return;
		}
		skills.setLevel(3, skills.getLevel(3) + amount);
		actionSender.sendSkillLevel(3);
	}

	@Override
	public boolean isAutoRetaliating() {
//		return playerDetails.isAutoRetaliate();
		return true;
	}

	@Override
	public boolean isValid() {
		return !World.getInstance().getPlayerList().contains(this);
	}

	@Override
	public void setEntityFocus(int id) {
		this.entityFocus = new EntityFocus(id);
		updateFlags.setEntityFocusUpdateRequired(true);
	}

	@Override
	public void setFaceLocation(Location location) {
		this.faceLocation = new FaceLocation(location);
		updateFlags.setFaceLocationUpdateRequired(true);
	}

	@Override
	public void setHp(int val) {
		this.getSkills().setLevel(3, val);
		actionSender.sendSkillLevel(3);
	}
	
	public void logout() {
//		if (!Combat.isXSecondsSinceCombat(this, getLastAttacked(), 10000)) {
//			getActionSender().sendMessage("You must have been out of combat for 10 seconds before you may log out.");
//			return;
//		}
		getSession().write(new StaticPacketBuilder().setId(86).toPacket()).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture current) {
				current.getSession().close();
			}
		});
	}

	public void forceLogout() {
		getSession().write(new StaticPacketBuilder().setId(86).toPacket()).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				arg0.getSession().close();
			}
		});
	}
	
	public void setNextAnimation(Animation animation) {
		setLastAnimation(animation);
		getUpdateFlags().setAnimationUpdateRequired(true);
	}
	
	public void setNextGraphic(Graphics graphic) {
		setLastGraphics(graphic);
		getUpdateFlags().setGraphicsUpdateRequired(true);
	}
}