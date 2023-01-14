package com.rs.entity.actor.npc;

import java.util.LinkedList;
import java.util.Queue;

import com.rs.entity.EntityType;
import com.rs.entity.Follow;
import com.rs.entity.actor.Actor;
import com.rs.entity.actor.attribute.Attribute;
import com.rs.entity.actor.player.Player;
import com.rs.net.definitions.NPCDefinition;
import com.rs.net.entity.masks.Animation;
import com.rs.net.entity.masks.EntityFocus;
import com.rs.net.entity.masks.FaceLocation;
import com.rs.net.entity.masks.ForceText;
import com.rs.net.entity.masks.Graphics;
import com.rs.net.entity.masks.Hits;
import com.rs.net.entity.masks.Hits.Hit;
import com.rs.utility.Utility;
import com.rs.world.Location;
import com.rs.world.World;

/**
 * Represents a 'non-player' character in the game.
 *
 * @author Graham
 * @author Luke132
 */
public class NPC extends Actor {

    public static enum WalkType {
        STATIC,
        WALK,
    }

    private int id;
    private transient NPCDefinition definition;
    private transient NPCUpdateFlags updateFlags;
    private transient EntityFocus entityFocus;
    private transient Animation lastAnimation;
    private transient Graphics lastGraphics;
    private transient ForceText forceText;
    private transient FaceLocation faceLocation;
    private transient Queue<Hit> queuedHits;
    private transient int hp;
    private transient boolean destroy;
    private transient Player owner;
    private WalkType walkType;
    private transient Location spawnLocation;
    private Location minimumCoords = Location.location(0, 0, 0);
    private Location maximumCoords = Location.location(0, 0, 0);
    private int faceDirection = FaceDirection.NORTH;

    public NPC(int id) {
        this.id = id;
        this.definition = NPCDefinition.forId(id);
        this.setWalkType(WalkType.WALK);
        this.faceDirection = FaceDirection.NORTH;
    }
    
    public NPC(int id, Location location) {
    	this(id);
    	this.location = location;
    }

    public Object readResolve() {
        super.readResolve(EntityType.NPC);
        definition = NPCDefinition.forId(id);
        updateFlags = new NPCUpdateFlags();
        this.setFollow(new Follow(this));
        this.queuedHits = new LinkedList<Hit>();
        this.hp = definition.getHitpoints();
        this.spawnLocation = getLocation();
        return this;
    }

    @Override
    public void tick() {
        getSprite().setSprites(-1, -1);
        int sprite = -1;
        if (getFollow().getFollowing() != null && !getAttributes().get(Attribute.FROZEN).getBoolean()) {
            getFollow().followEntity();
            return;
        }
        if (Math.random() > 0.8 && walkType == WalkType.WALK && !this.inCombat() && !getAttributes().get(Attribute.DEAD).getBoolean() && !getAttributes().get(Attribute.FROZEN).getBoolean() && id != 0) {
            int moveX = (int) (Math.floor((Math.random() * 3)) - 1);
            int moveY = (int) (Math.floor((Math.random() * 3)) - 1);
            int tgtX = this.getLocation().getX() + moveX;
            int tgtY = this.getLocation().getY() + moveY;
            sprite = Utility.direction(this.getLocation().getX(), this.getLocation().getY(), tgtX, tgtY);
            if (tgtX > this.maximumCoords.getX() || tgtX < this.minimumCoords.getX() || tgtY > this.maximumCoords.getY() || tgtY < this.minimumCoords.getY()) {
                sprite = -1;
            }
            if (sprite != -1) {
                sprite >>= 1;
                faceDirection = sprite;
                getSprite().setSprites(sprite, -1);
                this.setLocation(Location.location(tgtX, tgtY, this.getLocation().getZ()));
            }
            return;
        }
    }

    @Override
    public void dropLoot() {
       
    }


    public void processQueuedHits() {
        if (!updateFlags.isHitUpdateRequired()) {
            if (queuedHits.size() > 0) {
                Hit h = queuedHits.poll();
                hit(h.getDamage(), h.getType());
            }
        }
        if (!updateFlags.isHit2UpdateRequired()) {
            if (queuedHits.size() > 0) {
                Hit h = queuedHits.poll();
                hit(h.getDamage(), h.getType());
            }
        }
    }

    public void hit(int damage) {
        if (getAttributes().get(Attribute.DEAD).getBoolean()) {
            damage = 0;
        }
        hit(damage, damage <= 0 ? Hits.HitType.NO_DAMAGE : Hits.HitType.NORMAL_DAMAGE);
    }

    public void hit(int damage, Hits.HitType type) {
        boolean damageOverZero = damage > 0;
        if (damage > hp) {
            damage = hp;
        }
        if (damageOverZero && damage == 0) {
            type = Hits.HitType.NO_DAMAGE;
        }
        if (!updateFlags.isHitUpdateRequired()) {
            getHits().setHit1(new Hit(damage, type));
            updateFlags.setHitUpdateRequired(true);
        } else {
            if (!updateFlags.isHit2UpdateRequired()) {
                getHits().setHit2(new Hit(damage, type));
                updateFlags.setHit2UpdateRequired(true);
            } else {
                queuedHits.add(new Hit(damage, type));
            }
        }
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            if (!getAttributes().get(Attribute.DEAD).getBoolean()) {
//                World.getInstance().registerEvent(new DeathEvent(this)); //TODO: Convert this
            }
        }
    }

    @Override
    public void heal(int amount) {
        this.hp += amount;
        if (hp >= this.getDefinition().getHitpoints()) {
            hp = this.getDefinition().getHitpoints();
        }
    }

    public Animation getLastAnimation() {
        return lastAnimation;
    }

    public void setLastAnimation(Animation lastAnimation) {
        this.lastAnimation = lastAnimation;
    }

    public Graphics getLastGraphics() {
        return lastGraphics;
    }

    public void setLastGraphics(Graphics lastGraphics) {
        this.lastGraphics = lastGraphics;
    }

    public FaceLocation getFaceLocation() {
        return faceLocation;
    }

    public void setFaceLocation(Location location) {
        this.faceLocation = new FaceLocation(location);
        updateFlags.setFaceLocationUpdateRequired(true);
    }

    public int getId() {
        return id;
    }

    public NPCDefinition getDefinition() {
        return definition;
    }

    public NPCUpdateFlags getUpdateFlags() {
        return updateFlags;
    }

    /**
     * @param minimumCoords the minimumCoords to set
     */
    public void setMinimumCoords(Location minimumCoords) {
        this.minimumCoords = minimumCoords;
    }

    /**
     * @return the minimumCoords
     */
    public Location getMinimumCoords() {
        return minimumCoords;
    }

    /**
     * @param walkType the walkType to set
     */
    public void setWalkType(WalkType walkType) {
        this.walkType = walkType;
    }

    @Override
    public EntityFocus getEntityFocus() {
        return entityFocus;
    }

    /**
     * @return the walkType
     */
    public WalkType getWalkType() {
        return walkType;
    }

    /**
     * @param maximumCoords the maximumCoords to set
     */
    public void setMaximumCoords(Location maximumCoords) {
        this.maximumCoords = maximumCoords;
    }

    /**
     * @return the maximumCoords
     */
    public Location getMaximumCoords() {
        return maximumCoords;
    }

    public void setEntityFocus(int id) {
        this.entityFocus = new EntityFocus(id);
        updateFlags.setEntityFocusUpdateRequired(true);
    }

    public void setForceText(ForceText forceText) {
        this.forceText = forceText;
        updateFlags.setForceTextUpdateRequired(true);
    }

    public ForceText getForceText() {
        return forceText;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return this.definition.getHitpoints();
    }

    @Override
    public void setHp(int val) {
        hp = val;
    }

    @Override
    public int getAttackAnimation() {
        return NPCDefinition.forId(id).getAttackAnimation();
    }

    @Override
    public int getAttackSpeed() {
//        if (getMiasmicEffect() > 0) {
//            return NPCDefinition.forId(id).getAttackSpeed() * 2;
//        }
        return NPCDefinition.forId(id).getAttackSpeed();
    }

    @Override
    public int getMaxHit() {
        return NPCDefinition.forId(id).getMaxHit();
    }

    @Override
    public int getDefenceAnimation() {
        return NPCDefinition.forId(id).getDefenceAnimation();
    }

    @Override
    public int getDeathAnimation() {
        return NPCDefinition.forId(id).getDeathAnimation();
    }

    @Override
    public boolean isAutoRetaliating() {
        return hp > 0;
    }

    @Override
    public int getHitDelay() {
//        return Animations.getNPCHitDelay(this);
    	return 1;
    }

    @Override
    public boolean isValid() {
        return !World.getInstance().getNpcList().contains(this);
    }

    public void setDestroy(boolean b) {
        this.destroy = true;
    }

    public boolean shouldDestroy() {
        return this.destroy;
    }

    public void setOwner(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getFaceDirection() {
        return faceDirection;
    }

    public static final class FaceDirection {
        public static final int NORTH = 1;
        public static final int EAST = 4;
        public static final int SOUTH = 6;
        public static final int WEST = 3;
    }
}