
package de.jcing.world;

import java.awt.Graphics2D;

import de.jcing.Main;
import de.jcing.engine.io.Mouse;
import de.jcing.engine.window.SwingWindow;
import de.jcing.util.Point;

@Deprecated
public class Chunk {
	
	public static final int TILE_COUNT = 8;
	
	private Tile[][] tiles;
	private int x, y;
	
	private boolean loaded;
	
	private Stage stage;
	
	public Chunk(int x, int y, Stage stage) {
		this.x = x;
		this.y = y;
		this.stage = stage;
		tiles = new Tile[TILE_COUNT][TILE_COUNT];
		for (int xt = 0; xt < tiles.length; xt++) {
			for (int yt = 0; yt < tiles.length; yt++) {
				tiles[xt][yt] = new Tile(xt,yt,this);
			}
		}
	}
	
	public Chunk(Point p, Stage stage) {
		this(p.getXi(), p.getYi(), stage);
	}

	public void load(boolean loaded) {
		//System.out.println((loaded ? "loading" : "unloading") + " chunk " + new Point(x,y));
		this.loaded = loaded;
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public boolean isOnScreen() {
		return getXOffset() >= 0 && getXOffset() <= SwingWindow.PIXEL_WIDTH ||
				getXOffset() + TILE_COUNT*Tile.TILE_PIXELS >= 0 && getXOffset() + TILE_COUNT*Tile.TILE_PIXELS <= SwingWindow.PIXEL_WIDTH ||
				getYOffset() >= 0 && getYOffset() <= SwingWindow.PIXEL_HEIGHT ||
				getYOffset() + TILE_COUNT*Tile.TILE_PIXELS >= 0 && getYOffset() + TILE_COUNT*Tile.TILE_PIXELS <= SwingWindow.PIXEL_HEIGHT;
	}
	
	public boolean isHovered() {
		int mx = Mouse.getX();
		int my = Mouse.getY();
		return getXOffset() * Main.getWindow().getPixelWidth() <= mx && (getXOffset() + TILE_COUNT*Tile.TILE_PIXELS)* Main.getWindow().getPixelWidth() >= mx &&
				getYOffset() * Main.getWindow().getPixelHeight() <= my && (getYOffset() + TILE_COUNT*Tile.TILE_PIXELS)* Main.getWindow().getPixelHeight() >= my;
	}
	
	public void incAll() {
		for (int xt = 0; xt < tiles.length; xt++) {
			for (int yt = 0; yt < tiles.length; yt++) {
				tiles[xt][yt].incrementIndex();
			}
		}
	}
	
	public int getXOffset() {
		return (int) (x * TILE_COUNT * Tile.TILE_PIXELS - stage.getFixedCamera().getXd()*Main.getWindow().getPixelWidth());
	}
	
	public int getYOffset() {
		return (int) (y * TILE_COUNT * Tile.TILE_PIXELS - stage.getFixedCamera().getYd()*Main.getWindow().getPixelHeight());
	}
	
	public Point computeOffset(Point camera) {
		return new Point((x * TILE_COUNT * Tile.TILE_PIXELS - camera.getXd()*Main.getWindow().getPixelWidth()),
				(y * TILE_COUNT * Tile.TILE_PIXELS - camera.getYd()*Main.getWindow().getPixelHeight()));
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void draw(Graphics2D g, Point camera) {
		Point offset = computeOffset(camera);
		if(isOnScreen()) {
			for (int xt = 0; xt < tiles.length; xt++) {
				for (int yt = 0; yt < tiles.length; yt++) {
					tiles[xt][yt].draw(g, offset);
				}
			}
		}
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public void click() {
		for (int xt = 0; xt < tiles.length; xt++) {
			for (int yt = 0; yt < tiles.length; yt++) {
				if(tiles[xt][yt].hovered()) {
					tiles[xt][yt].incrementIndex();
					return;
				}
			}
		}
	}
	
}
