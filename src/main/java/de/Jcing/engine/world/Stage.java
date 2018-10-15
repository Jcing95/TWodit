package de.Jcing.engine.world;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedList;

import de.Jcing.Main;
import de.Jcing.engine.entity.Entity;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.tasks.Clock;
import de.Jcing.util.Point;

public class Stage implements Drawable{

	private HashMap<Point, Chunk> chunks;
	private HashMap<Integer, Entity> entities;
	private LinkedList<Integer> loadedChunks;
	
	private Point camera;
	
	//TODO: implement and manage tilesets!
	
	public Stage() {
		chunks = new HashMap<>();
		entities = new HashMap<>();
		loadedChunks = new LinkedList<>();
	}
	
	public void tick() {
		for (Entity e: entities.values())
			e.tick();
	}
	
	@Override
	public void draw(Graphics2D g) {
		//update stage camera here for consistent offset during rendering.
		camera = Main.getGame().getCamera().clone();
		
		Runnable[] chunkDrawings = new Runnable[chunks.size()];
		int i = 0;
		
		for(Point pt : chunks.keySet()) {
			chunkDrawings[i] = () -> chunks.get(pt).draw(g);
			i++;
		}
		
		Clock.schedule(true, chunkDrawings);
		
		for(Integer e : entities.keySet())
			entities.get(e).draw(g);
	}
	
	public void addChunk(int x, int y) {
		chunks.put(new Point(x,y), new Chunk(x,y,this));
	}
	
	public Chunk getChunk(Point point) {
		return chunks.get(point);
	}
	
	public Chunk getChunkAtWorldPos(double x, double y) {
		//if negative: decrement against "-0"
		int xChunk = (int) (x/Chunk.TILE_COUNT);
		if(x < 0)
			xChunk--;
		
		int yChunk = (int) (y/Chunk.TILE_COUNT);
		if(y < 0)
			yChunk --;
		return chunks.get(new Point(xChunk, yChunk));
	}
	
	public Tile getTileAtWorldPos(double x, double y) {
		int xTile = (int)x % Chunk.TILE_COUNT;
		int yTile = (int)y % Chunk.TILE_COUNT;
		Chunk ch = getChunkAtWorldPos(x,y);
		if(ch == null)
			return null;
		return ch.getTile(xTile,yTile);
	}
	
	public void removeChunk(int x, int y) {
		removeChunk(new Point(x,y));
	}
	
	public void removeChunk(Point point) {
		chunks.remove(point);
	}
	
	public void handleClick() {
		//TODO: implement chunk clicks
	}
	
	public void handleMouseMovement() {
		//TODO: implement chunk mouse movement
	}
	
	public int addEntity(Entity entity) {
		entities.put(entity.hashCode(), entity);
		return entity.hashCode();
	}

	public Point getCamera() {
		return camera;
	}

	public void setCamera(Point camera) {
		this.camera = camera;
	}
	
	
	
	
}
