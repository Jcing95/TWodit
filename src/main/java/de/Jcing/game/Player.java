package de.jcing.game;

import de.jcing.engine.entity.Entity;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;

public class Player extends Entity {

	private int anim_left, anim_right, anim_up, anim_down;
	
	public Player(float x, float y, float w, float h, TextureAssembler assembler) {
		super(x, y, w, h);
		
		anim_left = assembler.addFrames(new JAnimation("gfx/player/left/"), initTex);
		anim_right = assembler.addFrames(new JAnimation("gfx/player/right/"), initTex);
		anim_up = assembler.addFrames(new JAnimation("gfx/player/up/"), initTex);
		anim_down = assembler.addFrames(new JAnimation("gfx/player/down/"), initTex);

	}
	
	private AtlasCallback initTex = (id, atlas) -> {
		
	};
	
	
}
