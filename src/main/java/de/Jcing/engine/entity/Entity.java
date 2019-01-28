package de.jcing.engine.entity;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import de.jcing.Main;
import de.jcing.geometry.Rectangle;
import de.jcing.image.Animation;
import de.jcing.image.Image;
import de.jcing.util.Point;
import de.jcing.utillities.log.Log;
import de.jcing.world.Stage;
import de.jcing.world.Tile;

public class Entity {
	
	private static final Log LOG = new Log(Entity.class);

	public static final float DRAG = 0.75f;
	public static final float MAXSPEED = 10;
	
	public static final int ON_UP = 0;
	public static final int ON_LEFT = 1;
	public static final int ON_RIGHT = 2;
	public static final int ON_DOWN = 3;
	
	protected int animationIndex;
	
	protected HashMap<Integer, Animation> sprite;

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
			occupiedTiles.add(stage.getTileAtWorldPos(pt.getXd() + x, pt.getYd() + y));
		}
		return occupiedTiles;
	}
	
	
	
	public void tick() {
		nextX = position.getXd() + speedX;
		nextY = position.getYd() + speedY;
		
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
		
		position.setX(nextX);
		position.setY(nextY);
		
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
	
	
	public Rectangle getFootPrint() {
		return collisionBox;
	}


	// TODO: implement Entity logics
	// register at all tiles via Stage.getTileAtWorldPos()
	// if Entity size > Tile Size go with EntitySize/TileSize steps
	// for collision go in BiggerRect/smallerRect steps;

	public void draw(Graphics2D g, Point camera) {
//		g.setColor(Color.CYAN);
		int xPos = (int)(position.getXd()*Tile.TILE_PIXELS/Main.getWindow().getPixelWidth()-(camera.getXd())); // (int) (stage.getCamera().x * Main.getWindow().getPixelSize() - x*Tile.TILE_PIXELS);
		int yPos = (int)(position.getYd()*Tile.TILE_PIXELS/Main.getWindow().getPixelHeight()-(camera.getYd())); // (int) (stage.getCamera().y * Main.getWindow().getPixelSize() - y*Tile.TILE_PIXELS);
//		
//		// System.out.println(" cam: " + xPos + " | " + yPos);
//
//		g.fillRect(xPos, yPos, w, h);
		g.translate(xPos,yPos);
		if(sprite.containsKey(animationIndex)) {
			if(speedX != 0 || speedY != 0) {
				g.drawImage(sprite.get(animationIndex).get(), 0, 0, null);
			} else {
				g.drawImage(sprite.get(animationIndex).get(0), 0, 0, null);
			}
		}
		g.translate(-xPos, -yPos);
	}

	public double getX() {
		return position.getXd();
	}
	
	public double getY() {
		return position.getYd();
	}
	
	public Point getPosition() {
		return position;
	}

	public void setAnim(int on, Animation img) {
		sprite.put(on, img);
	}
	
}
