package de.jcing.world;

import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;

import de.jcing.Main;
import de.jcing.engine.entity.Entity;
import de.jcing.engine.graphics.Drawable;
import de.jcing.util.Point;
import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Task;
import de.jcing.window.Window;

public class Stage implements Drawable {

	private static final Log log = new Log(Stage.class);

	public static int loadingWidth = 7;
	public static int loadingHeight = 5;

	private HashMap<Point, Chunk> chunks;
	private HashMap<Integer, Entity> entities;
	private HashSet<Point> loadedChunks;

	private Point camera;
	private Point fixedCamera;
	private Point loadingAnchor;

	private Point lastAnchor;

	// TODO: implement and manage tilesets!

	public Stage() {
		log.debug("initializing Stage...");
		chunks = new HashMap<>();
		entities = new HashMap<>();
		loadedChunks = new HashSet<>();
		loadingAnchor = new Point(0, 0);
		lastAnchor = loadingAnchor.clone();
		camera = new Point(0,0);
		fixedCamera = camera.clone();
		log.debug("stage set up, loading world...");
		updateChunks();
	}

	public void setLoadingAnchor(Point pt) {
		this.loadingAnchor = pt;
	}

	public void tick() {
	
		if (lastAnchor.xDist(loadingAnchor) > 4 || lastAnchor.yDist(loadingAnchor) > 2) {
			lastAnchor = loadingAnchor.clone();
			new Task(() -> updateChunks()).name("chunkloader").start();
		}
		for (Entity e : entities.values())
			e.tick();

	}

	private void updateChunks() {
		final HashSet<Point> nextLoaded = new HashSet<>();
		final HashSet<Point> currentLoaded = loadedChunks;

		for (int x = loadingAnchor.getXi() - loadingWidth; x < loadingAnchor.getXi() + loadingWidth; x++) {
			for (int y = loadingAnchor.getYi() - loadingHeight; y < loadingAnchor.getYi() + loadingHeight; y++) {
				nextLoaded.add(new Point(x, y));
				load(new Point(x, y));
			}
		}
		
		loadedChunks = nextLoaded;
		for (Point p : currentLoaded) {
			if (!nextLoaded.contains(p)) {
				unload(p);
			}
		}
	}

	private void unload(Point p) {
		if (chunks.containsKey(p)) {
			chunks.get(p).load(false);
			chunks.remove(p);
		}
	}

	private void load(Point p) {
		if (!chunks.containsKey(p))
			chunks.put(p, new Chunk(p.getXi(), p.getYi(), this));
		chunks.get(p).load(true);
	}

	@Override
	public void draw(Graphics2D g) {
		// update stage camera here for consistent offset during rendering.
		fixedCamera = camera.clone();
		if (Main.getGame() != null && Main.getGame().isInitialized()) {

			try {
				for (Point p : loadedChunks) {
					Chunk c = chunks.get(p);
					if (c != null)
						c.draw(g, fixedCamera);
				}
			} catch (ConcurrentModificationException e) {
				System.err.println("mod!");
			}

			for (Integer e : entities.keySet())
				entities.get(e).draw(g, fixedCamera);
		}
	}

	public void addChunk(int x, int y) {
		chunks.put(new Point(x, y), new Chunk(x, y, this));
	}

	public Chunk getChunk(Point point) {
		return chunks.get(point);
	}

	public Chunk getChunkAtWorldPos(double x, double y) {
		return chunks.get(getChunkPosFromWorldPos(x, y));
	}

	public Point getChunkPosFromWorldPos(double x, double y) {
		// if negative: decrement against "-0"
		int xChunk = (int) (x / Chunk.TILE_COUNT);
		if (x < 0)
			xChunk--;

		int yChunk = (int) (y / Chunk.TILE_COUNT);
		if (y < 0)
			yChunk--;
		return new Point(xChunk, yChunk);
	}

	public Point getChunkPosFromPixel(double x, double y) {

		int xChunk = (int) ((x + getFixedCamera().getXd() - Window.PIXEL_WIDTH / 2) / Main.getWindow().getPixelWidth()
				/ Chunk.TILE_COUNT * Tile.TILE_PIXELS);
		int yChunk = (int) ((y + getFixedCamera().getYd() - Window.PIXEL_HEIGHT / 2) / Main.getWindow().getPixelHeight()
				/ Chunk.TILE_COUNT * Tile.TILE_PIXELS);

		// System.out.println("x: " + x + " y: " + y + " c: " + xChunk + "|" + yChunk +
		// " @cam " + getCamera() + " pxs: " + Main.getWindow().getPixelSize() + " TC: "
		// + Chunk.TILE_COUNT + " TP: " + Tile.TILE_PIXELS);
		if (x + getFixedCamera().getXd() < 0)
			xChunk--;
		if (y + getFixedCamera().getYd() < 0)
			yChunk--;
		return new Point(xChunk, yChunk);
	}

	public Tile getTileAtWorldPos(double x, double y) {
		int xTile = (int) x % Chunk.TILE_COUNT;
		int yTile = (int) y % Chunk.TILE_COUNT;
		Chunk ch = getChunkAtWorldPos(x, y);
		if (ch == null)
			return null;
		return ch.getTile(xTile, yTile);
	}

	public void removeChunk(int x, int y) {
		removeChunk(new Point(x, y));
	}

	public void removeChunk(Point point) {
		chunks.remove(point);
	}

	public void handleClick() {
		for (Chunk c : chunks.values()) {
			if (c.isHovered()) {
				c.click();
				return;
			}
		}
	}

	public int addEntity(Entity entity) {
		entities.put(entity.hashCode(), entity);
		return entity.hashCode();
	}

	public Point getFixedCamera() {
		return fixedCamera;
	}

	public void setCamera(Point camera) {
		this.camera = camera;
	}

}
