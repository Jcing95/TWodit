package de.jcing.game;

import org.lwjgl.glfw.GLFW;

import de.jcing.engine.entity.Creature;
import de.jcing.engine.image.generation.TextureFactory.TextureBuilder;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.opengl.Camera;
import de.jcing.engine.opengl.mesh.Mesh;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Player extends Creature {

	private static final float DEFAULT_SPEED = 0.1f;
	private static final float SHIFT_MULT = 2.5f;

	public Player(Mesh mesh, TextureBuilder b) {
		super(mesh);
		this.setAnim(ANIM.WALK_LEFT, b.getAnimation("left"));
		this.setAnim(ANIM.WALK_RIGHT, b.getAnimation("right"));
		this.setAnim(ANIM.WALK_UP, b.getAnimation("up"));
		this.setAnim(ANIM.WALK_DOWN, b.getAnimation("down"));
		currAnim = b.getAnimation("down");
	}

	public void tick(Camera cam) {
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
		cam.getPosition().y = position.y;
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

}
