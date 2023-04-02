package de.jcing.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;

import de.jcing.engine.image.texture.Animation;
import de.jcing.geometry.Rectangle;

public class Creature extends Entity {

	public static final float DRAG = 0.6f;
	public static final float MAXSPEED = 10;

	public enum ANIM {
		WALK_UP, WALK_LEFT, WALK_RIGHT, WALK_DOWN,
	}

	protected ANIM animationIndex;

	protected final HashMap<ANIM, Animation> sprite;

	protected Animation currAnim;

	protected Rectangle collisionBox;

	protected float w, h;

	protected float speedX, speedY;

	protected float accelerationX, accelerationY;

	protected final ArrayList<Runnable> onTick;

	public Creature() {
		super();
		onTick = new ArrayList<>();
		sprite = new HashMap<>();
	}

	public void tick() {

		movePosition(speedX, speedY, 0);

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
		accelerationX = 0;
		accelerationY = 0;

		if (speedX > 0)
			animationIndex = ANIM.WALK_RIGHT;
		if (speedX < 0)
			animationIndex = ANIM.WALK_LEFT;
		if (speedY < 0)
			animationIndex = ANIM.WALK_DOWN;
		if (speedY > 0)
			animationIndex = ANIM.WALK_UP;

		currAnim.set(sprite.get(animationIndex));
		if (speedX == 0 && speedY == 0)
			standing();
		else
			walking();

		for (Runnable r : onTick) {
			r.run();
		}
	}

	public void walking() {
		currAnim.update();
	}

	public void standing() {
		currAnim.reset();
	}

	public ArrayList<Runnable> getOntick() {
		return onTick;
	}

	public void accelerate(float x, float y) {
		accelerationX += x;
		accelerationY += y;
	}

	public Rectangle getFootPrint() {
		return collisionBox;
	}

	public void setAnim(ANIM anim) {
		animationIndex = anim;
		currAnim = sprite.get(anim).clone();
	}

	public void setAnim(ANIM on, Animation img) {
		sprite.put(on, img);
	}

}
