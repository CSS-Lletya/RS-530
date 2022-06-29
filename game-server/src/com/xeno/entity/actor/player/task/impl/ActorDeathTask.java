package com.xeno.entity.actor.player.task.impl;

import com.xeno.entity.actor.Actor;
import com.xeno.entity.actor.npc.NPC;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.Task;
import com.xeno.world.World;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The parent class that handles the death process for all characters.
 * @param <T> the type of actor the death process is being executed for.
 * @author lare96 <http://github.com/lare96>
 * @author Dennis
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class ActorDeathTask<T extends Actor> extends Task {
	
	/**
	 * The actor who has died and needs the death process.
	 */
	private final T actor;
	
	/**
	 * The counter that will determine which part of the death process we are
	 * on.
	 */
	private int counter;
	
	/**
	 * Creates a new {@link ActorDeathTask}.
	 * @param actor the actor who has died and needs the death process.
	 */
	public ActorDeathTask(T actor) {
		super(1, true);
		this.actor = actor;
	}
	
	/**
	 * The part of the death process where the actor is prepared for the
	 * rest of the death process.
	 */
	public abstract void preDeath();
	
	/**
	 * The main part of the death process where the killer is found for the
	 * actor.
	 */
	public abstract void death();
	
	/**
	 * The last part of the death process where the actor is reset.
	 */
	public abstract void postDeath();
	
	@Override
	public final void execute() {
		switch(counter++) {
			case 1:
				preDeath();
				getActor().setDead(true);
			case 2:
				death();
				break;
			case 4:
				postDeath();
				getActor().setDead(false);
				this.stop();
				break;
		}
	}
	
	@Override
	public final void onException(Exception e) {
		e.printStackTrace();
		if(getActor().isPlayer()) {
			Player player = (Player) getActor();
			player.getActionSender().logout();
		} else {
			World.getInstance().removeNPC((NPC) getActor());
		}
	}
}