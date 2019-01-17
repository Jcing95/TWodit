package de.Jcing.engine.entity;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.Jcing.Main;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.engine.world.Stage;
import de.Jcing.engine.world.Tile;
import de.Jcing.geometry.Rectangle;
import de.Jcing.image.Image;
import de.Jcing.util.Point;

public class Entity implements Drawable {
	
	private static final Logger LOG = LoggerFactory.getLogger(Entity.class);

	public static final float DRAG = 0.75f;
	public static final float MAXSPEED = 10;
	
	public static final int ON_UP = 0;
	public static final int ON_LEFT = 1;
	public static final int ON_RIGHT = 2;
	public static final int ON_DOWN = 3;
	
	protected int animationIndex;
	
	protected HashMap<Integer, Image> sprite;

	protected Stage stage;

	protected Rectangle collisionBox;

	/**
	 * world position in Tiles.
	 */
	protected Point position;

	protected int w, h;

	protected float speedX, speedY;

	protected float accelerationX, accelerationY;

	protected Set<Tile> occupiedTiles;

	protected LinkedList<Point> tileOccupationMask;
	
	protected HashSet<Tile> occupied;
	
	protected LinkedList<Runnable> onTick;
	private double nextX;
	private double nextY;

	public Entity(Stage stage, double x, double y, int w, int h) {
		this.stage = stage;
		position = new Point(x,y);
		this.w = w;
		this.h = h;
		onTick = new LinkedList<>();
		tileOccupationMask = createOccupationMask(Tile.TILE_PIXELS);
		sprite = new HashMap<>();
		LOG.debug("creating entity at: " +  x + "|" + y + " w/ " + w + "*" + h + "px");
		
	}

	private LinkedList<Point> createOccupationMask(int MAXDELTA) {
		LinkedList<Point> occupationMask = new LinkedList<>();
		for (int y = 0; true; y += MAXDELTA) {
			if (y > h)
				y = h;
			for (int x = 0; true; x += MAXDELTA) {
				if (x > w)
					x = w;
				occupationMask.add(new Point(x, y));
				if (x == w)
					break;
			}
			if (y == h)
				break;
		}
		return occupationMask;
	}

	protected Set<Tile> checkTileOccupation(double x, double y) {
		HashSet<Tile> occupiedTiles = new HashSet<>();
		for (Point pt : tileOccupationMask) {
			occupiedTiles.add(stage.getTileAtWorldPos(pt.x + x, pt.y + y));
		}
		return occupiedTiles;
	}
	
	
	
	public void tick() {
		nextX = position.x + speedX;
		nextY = position.y + speedY;
		
//		Set<Tile> nextOccupiedTiles = checkTileOccupation(nextX, nextY);
//		
//		boolean positiveX = speedX > 0;
//		boolean positiveY = speedY > 0;
		
//		for(Tile t: nextOccupiedTiles) {
//			
//			if(t!= null && t.hasCollision()) {
//				if(positiveX) {
//					
//				}
//				if(positiveY) {
//					
//				}
//			}
//		}
		
//		boolean collided = checkCollision(nextOccupiedTiles) || checkCollision(occupiedTiles);
//		
//		if (collided) {
//			correctMovement(nextOccupiedTiles);
//		}

//		if (occupiedTiles != null) {
//			for (Tile t : occupiedTiles) {
//				if (!nextOccupiedTiles.contains(t))
//					t.leave(this);
//			}
//			
//			for (Tile t : nextOccupiedTiles) {
//				if (!occupiedTiles.contains(t))
//					t.enter(this);
//			}
//		}
		
//		occupiedTiles = nextOccupiedTiles;
		
		position.x = nextX;
		position.y = nextY;
		
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
			animationIndex = ON_RIGHT;
		if(speedX < 0)
			animationIndex = ON_LEFT;
		if(speedY > 0)
			animationIndex = ON_DOWN;
		if(speedY < 0)
			animationIndex = ON_UP;
		
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
	
	private boolean checkCollision(Set<Tile> nextOccupiedTiles) {
		// TODO implement collision check and movement correction
//		HashSet<Tile> collisionTiles = new HashSet<>();
//		HashSet<Entity> collidedEntities = new HashSet<>();
//		for(Tile t: nextOccupiedTiles) {
//			if(t == null)
//				continue;
//			if(t.hasCollision())
//				collisionTiles.add(t);
//			for(Entity e : t.getEntities()) {
//				if(e.getFootPrint().collides(getFootPrint()))
//					collidedEntities.add(e);
//			}
//		}
//		
//		return collisionTiles.isEmpty() && collidedEntities.isEmpty();
		return false;
	}
	
	public Rectangle getFootPrint() {
		return collisionBox;
	}

	private void correctMovement(Set<Tile> nextOccupiedTiles) {

	}

	// TODO: implement Entity logics
	// register at all tiles via Stage.getTileAtWorldPos()
	// if Entity size > Tile Size go with EntitySize/TileSize steps
	// for collision go in BiggerRect/smallerRect steps;

	@Override
	public void draw(Graphics2D g) {
//		g.setColor(Color.CYAN);
		int xPos = (int)(position.x*Tile.TILE_PIXELS/Main.getWindow().getPixelSize()-(Main.getGame().getCamera().x)); // (int) (stage.getCamera().x * Main.getWindow().getPixelSize() - x*Tile.TILE_PIXELS);
		int yPos = (int)(position.y*Tile.TILE_PIXELS/Main.getWindow().getPixelSize()-(Main.getGame().getCamera().y)); // (int) (stage.getCamera().y * Main.getWindow().getPixelSize() - y*Tile.TILE_PIXELS);
//		
//		// System.out.println(" cam: " + xPos + " | " + yPos);
//
//		g.fillRect(xPos, yPos, w, h);
		g.translate(xPos,yPos);
		if(sprite.containsKey(animationIndex)) {
			if(speedX != 0 || speedY != 0) {
				g.drawImage(sprite.get(animationIndex).get().get(), 0, 0, null);
			} else {
				g.drawImage(sprite.get(animationIndex).get(0).get(), 0, 0, null);
			}
		}
		g.translate(-xPos, -yPos);
	}

	public double getX() {
		return position.x;
	}
	
	public double getY() {
		return position.y;
	}
	
	public Point getPosition() {
		return position;
	}

	public void setAnim(int on, Image img) {
		sprite.put(on, img);
	}
	
}
