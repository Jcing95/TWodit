package de.jcing.game;

import org.lwjgl.glfw.GLFW;

import de.jcing.engine.entity.Creature;
import de.jcing.engine.gl.Camera;
import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.MeshFactory;
import de.jcing.engine.image.JAnimation;
import de.jcing.engine.image.texture.Animation;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.io.KeyBoard;

public class Player extends Creature {
	
	private static final float DEFAULT_SPEED = 0.2f;
	private static final float SHIFT_MULT = 2.5f;

	private int anim_left, anim_right, anim_up, anim_down;

	private Animation left, right, up, down;

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
		left = assembler.getAnimation(anim_left);
		right = assembler.getAnimation(anim_right);
		up = assembler.getAnimation(anim_up);
		down = assembler.getAnimation(anim_down);
		setMesh(new Mesh(up, MeshFactory.createRectData(0, 0, 0.1f, 1, 1, up)));
		System.out.println("player set up");
	};

	public void tick(Camera cam) {
		
		up.update();
		
		float speed = DEFAULT_SPEED;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			speed *= SHIFT_MULT;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_W))
			movePosition(0, speed, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_A))
			movePosition(-speed, 0, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_S))
			movePosition(0, -speed, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_D))
			movePosition(speed, 0, 0);
		
		cam.getPosition().x = position.x;
		cam.getPosition().y = position.y-3.5f;
		
		
	}
	
	@Override
	public Mesh getMesh() {
		return mesh;
	}

}
