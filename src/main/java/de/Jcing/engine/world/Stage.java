package de.jcing.engine.world;

import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;

import de.jcing.Main;
import de.jcing.engine.entity.Entity;
import de.jcing.engine.graphics.Drawable;
import de.jcing.util.Point;
import de.jcing.utillities.log.Log;
import de.jcing.window.Window;

public class Stage implements Drawable {
	
	private static final Log LOG = new Log(Stage.class);
	
	public static int loadingWidth = 5;
	public static int loadingHeight = 5;

	private HashMap<Point, Chunk> chunks;
	private HashMap<Integer, Entity> entities;
	private HashSet<Point> loadedChunks;
	
	private Point camera;
	
	private Point loadingAnchor;
	
	//TODO: implement and manage tilesets!
	
	public Stage() {
		chunks = new HashMap<>();
		entities = new HashMap<>();
		loadedChunks = new HashSet<>();
		loadingAnchor = new Point(0,0);
	}
	
	public void setLoadingAnchor(Point pt) {
		this.loadingAnchor = pt;
	}
	
	public void tick() {
		
		updateChunks();
		for (Entity e: entities.values())
			e.tick();
				
	}
	
	private void updateChunks() {
		final HashSet<Point> nextLoaded = new HashSet<>();
		for(int x = loadingAnchor.getXi()-loadingWidth; x < loadingAnchor.getXi()+loadingWidth;x++) {
			for(int y = loadingAnchor.getYi()-loadingHeight; y < loadingAnchor.getYi()+loadingHeight;y++) {
				nextLoaded.add(new Point(x,y));
				load(new Point(x,y));
			}
		}
		
		final HashSet<Point> currentLoaded = loadedChunks;
		loadedChunks = nextLoaded;
		
		for(Point p: currentLoaded) {
			if(!nextLoaded.contains(p)) {
				unload(p);
			}
		}

		
	}
	
	private void unload(Point p) {
		if(chunks.containsKey(p)) {
			chunks.get(p).load(false);
			chunks.remove(p);
		}
	}
	
	private void load(Point p) {
		if(!chunks.containsKey(p))
			chunks.put(p, new Chunk(p.getXi(), p.getYi(),this));
		chunks.get(p).load(true);
	}
	
	private Point getChunkPosFromPixel(Point point) {
		return getChunkPosFromPixel(point.x, point.y);
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
	
	public Point getChunkPosFromPixel(double x, double y) {
		
		int xChunk = (int) ((x + getCamera().x - Window.PIXEL_WIDTH/2) / Main.getWindow().getPixelSize() / Chunk.TILE_COUNT * Tile.TILE_PIXELS);
		int yChunk = (int) ((y + getCamera().y - Window.PIXEL_HEIGHT/2) / Main.getWindow().getPixelSize() / Chunk.TILE_COUNT * Tile.TILE_PIXELS);
		
		//System.out.println("x: " + x + " y: " + y + " c: " + xChunk + "|" + yChunk + " @cam " + getCamera() + " pxs: " + Main.getWindow().getPixelSize() + " TC: " + Chunk.TILE_COUNT + " TP: " + Tile.TILE_PIXELS);
		if(x + getCamera().x < 0)
			xChunk--;
		if(y + getCamera().y < 0)
			yChunk--;
		return new Point(xChunk, yChunk);
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
