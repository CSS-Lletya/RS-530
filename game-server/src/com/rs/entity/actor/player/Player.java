package com.rs.entity.actor.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;

import com.rs.content.Clan;
import com.rs.content.ShopSession;
import com.rs.content.TradeSession;
import com.rs.content.mapzone.MapZone;
import com.rs.content.mapzone.MapZoneManager;
import com.rs.entity.EntityType;
import com.rs.entity.Follow;
import com.rs.entity.WalkingQueue;
import com.rs.entity.actor.Actor;
import com.rs.entity.actor.attribute.Attribute;
import com.rs.entity.actor.player.task.LinkedTaskSequence;
import com.rs.net.ActionSender;
import com.rs.net.Packet;
import com.rs.net.definitions.NPCDefinition;
import com.rs.net.entity.LocalEntityList;
import com.rs.net.entity.PlayerUpdateFlags;
import com.rs.net.entity.masks.Animation;
import com.rs.net.entity.masks.Appearance;
import com.rs.net.entity.masks.ChatMessage;
import com.rs.net.entity.masks.EntityFocus;
import com.rs.net.entity.masks.FaceLocation;
import com.rs.net.entity.masks.ForceMovement;
import com.rs.net.entity.masks.ForceText;
import com.rs.net.entity.masks.Graphics;
import com.rs.net.entity.masks.Hits;
import com.rs.net.entity.masks.Hits.Hit;
import com.rs.packetbuilder.StaticPacketBuilder;
import com.rs.packetbuilder.packets.OutgoingPacketDispatcher;
import com.rs.player.skills.Skills;
import com.rs.player.skills.magic.TeleportType;
import com.rs.player.skills.prayer.PrayerData;
import com.rs.player.skills.prayer.Prayers;
import com.rs.utility.RandomUtils;
import com.rs.world.Location;
import com.rs.world.World;

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
	private Bank bank;
	private Appearance appearance;
	private Equipment equipment;
	private Skills skills;
	private Inventory inventory;
	private Friends friends;
	private transient MapZoneManager mapZoneManager;
	private Optional<MapZone> currentMapZone;
	
	/**
	 * Represents a Player's last Emote delay (used for various things)
	 */
	private transient long nextEmoteEnd;
    
    public transient InterfaceManager interfaceManager;
	private transient Queue<Hit> queuedHits;
	private transient List<Player> tradeRequests;
	private transient List<Player> duelRequests;
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
		tradeRequests = new ArrayList<Player>();
		duelRequests = new ArrayList<Player>();
		playerCredentials.refreshLongName();
		queuedHits = new LinkedList<Hit>();
		interfaceManager = new InterfaceManager(this);
		mapZoneManager = new MapZoneManager();
		currentMapZone = Optional.empty();
		return this;
	}

	/**
	 * Called roughly every 600ms.
	 */
	@Override
	public void tick() {
//		if (this.inCombat()) {
//			Combat.combatLoop(this);
//		}
		getMapZoneManager().executeVoid(this, zone -> zone.process(this));
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
	
	public void forceChat(String message) {
		setForceText(new ForceText(message));
		updateFlags.setForceTextUpdateRequired(true);
	}
	
	public void hit(int damage) {
		hit(damage, damage == 0 ? Hits.HitType.NO_DAMAGE : Hits.HitType.NORMAL_DAMAGE);
	}
	
	public void hit(int damage, Hits.HitType type) {
		if (getAttributes().get(Attribute.DEAD).getBoolean()) {
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
				heal((int) (skills.getTrueLevel(5) * 0.25));
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
		if(damage > skills.getTrueLevel(3)) {
			damage = skills.getTrueLevel(3);
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
		skills.setLevel(3, skills.getTrueLevel(3) - damage);
		if(skills.getTrueLevel(3) <= 0) {
			skills.setLevel(3, 0);
			if(!getAttributes().get(Attribute.DEAD).getBoolean()) {
//				World.getInstance().registerEvent(new DeathEvent(this));//TODO: Finish converting
			}
		}
		actionSender.sendSkillLevel(3);
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
		return this.getSkills().getTrueLevel(3);
	}

	@Override
	public int getMaxHit() {
//        // float effectiveStr = (float) Math.floor((skills.getLevelForXp(2)) * 1/*prayer*/ * 1/*other*/ + 0/*style*/);
//        /*float baseDmg = 1.3F + (effectiveStr / 9.5F) + (bonuses.getBonus(11) / 8F) + ((effectiveStr * bonuses.getBonus(11)) / 64F);
//        return (int) baseDmg * bonuses.getBonus(11);*/
//		int a = skills.getLevelForXp(2);
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
//			int strength = skills.getLevelForXp(2);
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
//			int range = skills.getLevelForXp(4);
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
		return this.getSkills().getTrueLevel(3);
	}

	@Override
	public void heal(int amount) {
		if (getAttributes().get(Attribute.DEAD).getBoolean()) {
			return;
		}
		if ((skills.getTrueLevel(3) + amount) > (skills.getTrueLevel(3))) {
			skills.setLevel(3, skills.getTrueLevel(3));
			actionSender.sendSkillLevel(3);
			return;
		}
		skills.setLevel(3, skills.getTrueLevel(3) + amount);
		actionSender.sendSkillLevel(3);
	}

	@Override
	public boolean isAutoRetaliating() {
//		return playerDetails.isAutoRetaliate();
		return true;
	}

	@Override
	public boolean isValid() {
		return World.getInstance().getPlayerList().contains(this);
	}

	//seems weird but okay
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
		getMapZoneManager().executeVoid(this, zone -> zone.logout(this));
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
	
	/**
	 * Queue Teleport type handling with Consumer support
	 * @param destination
	 * @param type
	 * @param player
	 */
	public void move(Location destination, TeleportType type, Consumer<Player> onFinish) {
		move((byte) 1, destination, type, onFinish);
	}
	
	public void move(byte delay, Location destination, TeleportType type, Consumer<Player> onFinish) {
		getAttributes().get(Attribute.LOCKED).set(true);
		LinkedTaskSequence seq = new LinkedTaskSequence(delay, true);
		seq.connect(1, () -> {
			type.getStartAnimation().ifPresent(this::setNextAnimation);
			type.getStartGraphic().ifPresent(this::setNextGraphic);
		}).connect(type.getEndDelay(), () -> {
			type.getEndAnimation().ifPresent(this::setNextAnimation);
			type.getEndGraphic().ifPresent(this::setNextGraphic);
			teleport(destination);
			onFinish.accept(this);
			getAttributes().get(Attribute.LOCKED).set(false);
		}).start();
	}
	
	/**
	 * Queue Teleport type handling
	 * @param destination
	 * @param type
	 * @param entity
	 */
	public void move(Location destination, TeleportType type) {
		move((byte) 1, destination, type);
	}
	
	public void move(byte delay, Location location, TeleportType type) {
		getAttributes().get(Attribute.LOCKED).set(true);
		LinkedTaskSequence seq = new LinkedTaskSequence(delay, true);
		seq.connect(1, () -> {
			type.getStartAnimation().ifPresent(this::setNextAnimation);
			type.getStartGraphic().ifPresent(this::setNextGraphic);
		}).connect(type.getEndDelay(), () -> {
			type.getEndAnimation().ifPresent(this::setNextAnimation);
			type.getEndGraphic().ifPresent(this::setNextGraphic);
			teleport(location);
			getAttributes().get(Attribute.LOCKED).set(false);
		}).start();
	}
	
	public void simpleTask(int delay, Consumer<Player> actor) {
		this.task(delay, tasker -> tasker.toPlayer());
	}
}