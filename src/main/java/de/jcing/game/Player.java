package de.jcing.game;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import de.jcing.engine.image.Animation;
import de.jcing.engine.image.generation.TextureBuilder.BuiltTexture;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.opengl.Camera;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.Sprite;

public class Player extends Sprite {

	public enum ANIM {
		WALK_UP, WALK_LEFT, WALK_RIGHT, WALK_DOWN,
	}

	private Vector3f position;

	private static final float DEFAULT_SPEED = 0.1f;
	private static final float SHIFT_MULT = 2.5f;

	public static final float DRAG = 0.6f;
	public static final float MAXSPEED = 10;

	private ANIM animationIndex;

	private final HashMap<ANIM, Animation> animations;

	private Animation currAnim;

	private float speedX;
	private float speedY;

	public Player(Mesh mesh, BuiltTexture b) {
		super(mesh);
		position = new Vector3f(0, 0, 0);
		animations = new HashMap<>();
		animations.put(ANIM.WALK_LEFT, b.getAnimation("left"));
		animations.put(ANIM.WALK_RIGHT, b.getAnimation("right"));
		animations.put(ANIM.WALK_UP, b.getAnimation("up"));
		animations.put(ANIM.WALK_DOWN, b.getAnimation("down"));
		animationIndex = ANIM.WALK_DOWN;
		currAnim = animations.get(animationIndex);
	}

	public void tick(Camera cam) {
		float speed = DEFAULT_SPEED;
		float accelerationX = 0;
		float accelerationY = 0;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			speed *= SHIFT_MULT;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_W))
			accelerationY = speed;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_A))
			accelerationX = -speed;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_S))
			accelerationY = -speed;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_D))
			accelerationX = speed;

		speedX *= DRAG;
		speedY *= DRAG;

		if (Math.abs(speedX) < 0.1)
			speedX = 0;
		if (Math.abs(speedY) < 0.1)
			speedY = 0;

		speedX = Float.min(speedX + accelerationX, MAXSPEED);
		speedY = Float.min(speedY + accelerationY, MAXSPEED);
		speedX = Float.max(speedX + accelerationX, -MAXSPEED);
		speedY = Float.max(speedY + accelerationY, -MAXSPEED);
		movePosition(speedX, speedY, 0);

		if (speedX > 0)
			animationIndex = ANIM.WALK_RIGHT;
		if (speedX < 0)
			animationIndex = ANIM.WALK_LEFT;
		if (speedY < 0)
			animationIndex = ANIM.WALK_DOWN;
		if (speedY > 0)
			animationIndex = ANIM.WALK_UP;

		currAnim = animations.get(animationIndex);

		if (speedX == 0 && speedY == 0)
			currAnim.reset();
		else
			currAnim.update();

		cam.getPosition().x = position.x;
		cam.getPosition().y = position.y;
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

	public Vector2f getTextureOffset() {
		return currAnim.getOffset();
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	public void movePosition(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

}
