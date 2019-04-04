package de.jcing.game;

import de.jcing.engine.entity.Entity;
import de.jcing.engine.gl.Camera;
import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.MeshFactory;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.Animation;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;

public class Player {

	private int anim_left, anim_right, anim_up, anim_down;
	
	private Animation left,right,up,down;
	
	public Player(float x, float y, float w, float h, TextureAssembler assembler) {
		
		anim_left = assembler.addFrames(new JAnimation("gfx/player/left/"), initTex);
		anim_right = assembler.addFrames(new JAnimation("gfx/player/right/"), initTex);
		anim_up = assembler.addFrames(new JAnimation("gfx/player/up/"), initTex);
		anim_down = assembler.addFrames(new JAnimation("gfx/player/down/"), initTex);

	}
	
	private AtlasCallback initTex = (id, assembler) -> {
		if(id == anim_left)
			left = assembler.getAnimation(id);
		if(id == anim_right)
			right = assembler.getAnimation(id);
		if(id == anim_up)
			up = assembler.getAnimation(id);
		if(id == anim_down)
			down = assembler.getAnimation(id);
		
		createMesh();
	};
	
	
	private void createMesh() {
		mesh = new Mesh(up, MeshFactory.createRectData(0, 0, 0, 1, 1, up));
	}
	
	private void tick(Camera cam) {
		
	}
	
	
}
