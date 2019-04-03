package de.jcing.game;

import de.jcing.engine.entity.Entity;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.Animation;

public class Player extends Entity {

	
	
	public Player(float x, float y, float w, float h) {
		super(x, y, w, h);
		
		JAnimation left = new JAnimation("gfx/player/left/");
				
//				player.setAnim(Entity.ON_LEFT, new Animation());
//		player.setAnim(Entity.ON_RIGHT, new Animation("gfx/player/right/"));
//		player.setAnim(Entity.ON_UP, new Animation("gfx/player/up/"));
//		player.setAnim(Entity.ON_DOWN, new Animation("gfx/player/down/"));
		
	}
	
	
	
}
