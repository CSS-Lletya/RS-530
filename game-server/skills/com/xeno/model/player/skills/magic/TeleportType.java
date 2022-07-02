package com.xeno.model.player.skills.magic;

import java.util.Optional;

import com.xeno.net.entity.masks.Animation;
import com.xeno.net.entity.masks.Graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TeleportType {
	
	NORMAL(7, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	ANCIENT(9, Optional.of(new Animation(9599)), Optional.empty(), Optional.of(new Graphics(1681)), Optional.empty()),
	LUNAR(10, Optional.of(new Animation(9606)), Optional.empty(), Optional.of(new Graphics(1685)), Optional.empty()),
	TABLET(4, Optional.of(new Animation(4731)), Optional.empty(), Optional.of(new Graphics(678)), Optional.empty()),
	LEVER(5, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	LADDER(3, Optional.of(new Animation(828)), Optional.empty(), Optional.empty(), Optional.empty()),
	DOOR(3, Optional.of(new Animation(9105)), Optional.empty(), Optional.empty(), Optional.empty()),
	
	;
	
	/**
	 * The ending delay for this teleport.
	 */
	@Getter
	private final int endDelay;
	
	/**
	 * The start animation of this teleport.
	 */
	@Getter
	private final Optional<Animation> startAnimation;
	
	/**
	 * The end animation of this teleport.
	 */
	@Getter
	private final Optional<Animation> endAnimation;
	
	/**
	 * The start graphic of this teleport.
	 */
	@Getter
	private final Optional<Graphics> startGraphic;
	
	/**
	 * The end graphic of this teleport.
	 */
	@Getter
	private final Optional<Graphics> endGraphic;
}