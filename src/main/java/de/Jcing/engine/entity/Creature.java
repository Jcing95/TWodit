package de.jcing.engine.entity;

import java.util.HashMap;
import java.util.LinkedList;

import org.joml.Vector2f;

import de.jcing.engine.image.texture.Animation;
import de.jcing.geometry.Rectangle;
import de.jcing.utillities.log.Log;
import de.jcing.world.Stage;

public class Creature {
	
	private static final Log LOG = new Log(Creature.class);

	public static final float DRAG = 0.75f;
	public static final float MAXSPEED = 10;
	
	public enum ANIM {
			WALK_UP,
			WALK_LEFT,
			WALK_RIGHT,
			WALK_DOWN,
	}
	
	protected ANIM animationIndex;
	
	protected HashMap<ANIM, Animation> sprite;

	protected Stage stage;

	protected Rectangle collisionBox;

	protected Vector2f position;

	protected float w, h;

	protected float speedX, speedY;

	protected float accelerationX, accelerationY;	
	
	protected LinkedList<Runnable> onTick;


	public Creature(float x, float y, float w, float h) {
		this.stage = stage;
		position = new Vector2f(x,y);
		this.w = w;
		this.h = h;
		onTick = new LinkedList<>();
//		tileOccupationMask = createOccupationMask(Tile.TILE_PIXELS);
		sprite = new HashMap<>();
		LOG.debug("creating entity at: " +  x + "|" + y + " w/ " + w + "*" + h + "px");
		
	}
	
	public void tick() {
		
		position.x += speedX;
		position.y += speedY;
		
		speedX *= DRAG;
		speedY *= DRAG;
		
		if(Math.abs(speedX) < 0.01)
			speedX = 0;
		if(Math.abs(speedY) < 0.01)
			speedY = 0;
		
		speedX = Float.min(speedX + accelerationX, MAXSPEED);
		speedY = Float.min(speedY + accelerationY, MAXSPEED);
		speedX = Float.max(speedX + accelerationX, -MAXSPEED);
		speedY = Float.max(speedY + accelerationY, -MAXSPEED);
		accelerationX = 0;
		accelerationY = 0;
		
		if(speedX > 0)
			animationIndex = ANIM.WALK_RIGHT;
		if(speedX < 0)
			animationIndex = ANIM.WALK_LEFT;
		if(speedY > 0)
			animationIndex = ANIM.WALK_DOWN;
		if(speedY < 0)
			animationIndex = ANIM.WALK_UP;
		
		for(Runnable r : onTick) {
			r.run();
		}
	}
	
	public LinkedList<Runnable> getOntick() {
		return onTick;
	}
	
	public void accelerate(float x, float y) {
		accelerationX += x;
		accelerationY += y;
	}
	
	
	public Rectangle getFootPrint() {
		return collisionBox;
	}

	
	public Vector2f getPosition() {
		return position;
	}

	public void setAnim(ANIM on, Animation img) {
		sprite.put(on, img);
	}
	
}
