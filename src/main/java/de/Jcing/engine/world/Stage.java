package de.Jcing.engine.world;

import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import de.Jcing.Main;
import de.Jcing.engine.entity.Entity;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.util.Point;

public class Stage implements Drawable {
	
	public static int loadedRadius = 5;

	private HashMap<Point, Chunk> chunks;
	private HashMap<Integer, Entity> entities;
	private HashSet<Point> loadedChunks;
	
	private Point camera;
	
	//TODO: implement and manage tilesets!
	
	public Stage() {
		chunks = new HashMap<>();
		entities = new HashMap<>();
		loadedChunks = new HashSet<>();
	}
	
	public void tick() {
		if(Main.getGame() != null && Main.getGame().isInitialized() && camera != null) {
			updateChunks();

			for (Entity e: entities.values())
				e.tick();
		}
	}
	
	private void updateChunks() {
		HashSet<Point> toRemove = new HashSet<>();
		for(Point p : loadedChunks) {
			if(p.distance(getChunkPosFromWorldPos(Main.getGame().getPlayer().getX(),Main.getGame().getPlayer().getY())) > loadedRadius) {
				chunks.get(p).load(false);
				toRemove.add(p);
			}
		}
		for(Point p : toRemove)
			loadedChunks.remove(p);
		for(int x = camera.getX()-loadedRadius; x < camera.getX()+loadedRadius; x++) {
			for(int y = camera.getY()-loadedRadius; y < camera.getY()+loadedRadius; y++) {
				Point p = getChunkPosFromWorldPos(camera).translate(new Point(x,y));
				if(p.distance(getChunkPosFromWorldPos(camera)) <= loadedRadius && !loadedChunks.contains(p)) {
					loadedChunks.add(p);
					if(!chunks.containsKey(p))
						chunks.put(p, new Chunk(p.getX(), p.getY(),this));
					chunks.get(p).load(true);
				}
			}
		}
	}
	
	private Point getChunkPosFromWorldPos(Point point) {
		return getChunkPosFromWorldPos(point.x, point.y);
	}

	@Override
	public void draw(Graphics2D g) {
		if(Main.getGame() != null && Main.getGame().isInitialized()) {
		//update stage camera here for consistent offset during rendering.
			camera = Main.getGame().getCamera().clone();
			
	//		Runnable[] chunkDrawings = new Runnable[chunks.size()];
	//		int i = 0;
	//		
	//		for(Point pt : chunks.keySet()) {
	//			chunkDrawings[i] = () -> chunks.get(pt).draw(g);
	//			i++;
	//		}
	//		
	//		Clock.schedule(true, chunkDrawings);
	//		Point cameraChunk = getChunkPosFromWorldPos(camera.x, camera.y);
	//		Set<Chunk> drawChunks = Pointmask.getFromPointMap(chunks, cameraChunk.getX(), cameraChunk.getY(), 3);
			try {
			for(Point p : loadedChunks) {
//				System.out.println("d: " + p);
				Chunk c = chunks.get(p);
				if(c != null)
					c.draw(g);
			}
			} catch(ConcurrentModificationException e) {
				System.err.println("mod!");
			}
			
			for(Integer e : entities.keySet())
				entities.get(e).draw(g);
		}
	}
	
	public void addChunk(int x, int y) {
		chunks.put(new Point(x,y), new Chunk(x,y,this));
	}
	
	public Chunk getChunk(Point point) {
		return chunks.get(point);
	}
	
	public Chunk getChunkAtWorldPos(double x, double y) {
		return chunks.get(getChunkPosFromWorldPos(x, y));
	}
	
	public Point getChunkPosFromWorldPos(double x, double y) {
		//if negative: decrement against "-0"
		int xChunk = (int) (x/Chunk.TILE_COUNT);
		if(x < 0)
			xChunk--;
		
		int yChunk = (int) (y/Chunk.TILE_COUNT);
		if(y < 0)
			yChunk --;
		return new Point(xChunk,yChunk);
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
		for(Chunk c : chunks.values()) {
			if(c.isHovered()) {
				c.click();
				return;
			}
		}
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
