package de.jcing.game;

import org.lwjgl.glfw.GLFW;

import de.jcing.engine.entity.Creature;
import de.jcing.engine.gl.Camera;
import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.MeshFactory;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.io.KeyBoard;

public class Player extends Creature {
	
	private static final float DEFAULT_SPEED = 0.1f;
	private static final float SHIFT_MULT = 2.5f;

	private int anim_left, anim_right, anim_up, anim_down;


	public Player(TextureAssembler assembler) {
		super();
		anim_left = assembler.addFrames(new JAnimation("gfx/player/left/"));
		anim_right = assembler.addFrames(new JAnimation("gfx/player/right"));
		anim_up = assembler.addFrames(new JAnimation("gfx/player/up/"));
		anim_down = assembler.addFrames(new JAnimation("gfx/player/down/"));
//		position.z = 1;
		setRotation(-45, 0, 0);
		assembler.addCallback(initTex);
	}

	private AtlasCallback initTex = (assembler) -> {
		this.setAnim(ANIM.WALK_LEFT,assembler.getAnimation(anim_left));
		this.setAnim(ANIM.WALK_RIGHT,assembler.getAnimation(anim_right));
		this.setAnim(ANIM.WALK_UP,assembler.getAnimation(anim_up));
		this.setAnim(ANIM.WALK_DOWN,assembler.getAnimation(anim_down));
		setAnim(ANIM.WALK_UP);
		setMesh(new Mesh(currAnim, MeshFactory.createRectData(0, 0, 0.1f, 1, 1, currAnim, false)));
		System.out.println("player set up");
	};

	public void tick(Camera cam) {
		if(initialized) {
			float speed = DEFAULT_SPEED;
			float speedX = 0, speedY = 0;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
				speed *= SHIFT_MULT;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_W))
				speedY += speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_A))
				speedX -= speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_S))
				speedY -= speed;
			if (KeyBoard.isPressed(GLFW.GLFW_KEY_D))
				speedX += speed;
			
			accelerate(speedX, speedY);
			
			super.tick();
			
			cam.getPosition().x = position.x;
			cam.getPosition().y = position.y-3.5f;
		}
	}
	

	@Override
	public Mesh getMesh() {
		return mesh;
	}

}
