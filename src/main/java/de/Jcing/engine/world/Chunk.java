package de.jcing.engine.world;

import java.awt.Graphics2D;

import de.jcing.Main;
import de.jcing.engine.graphics.Drawable;
import de.jcing.engine.io.Mouse;
import de.jcing.util.Point;
import de.jcing.window.Window;

public class Chunk implements Drawable{
	
	public static final int TILE_COUNT = 4;
	
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
		return getXOffset() >= 0 && getXOffset() <= Window.PIXEL_WIDTH ||
				getXOffset() + TILE_COUNT*Tile.TILE_PIXELS >= 0 && getXOffset() + TILE_COUNT*Tile.TILE_PIXELS <= Window.PIXEL_WIDTH ||
				getYOffset() >= 0 && getYOffset() <= Window.PIXEL_HEIGHT ||
				getYOffset() + TILE_COUNT*Tile.TILE_PIXELS >= 0 && getYOffset() + TILE_COUNT*Tile.TILE_PIXELS <= Window.PIXEL_HEIGHT;
	}
	
	public boolean isHovered() {
		int mx = Mouse.getX();
		int my = Mouse.getY();
		return getXOffset() * Main.getWindow().getPixelSize() <= mx && (getXOffset() + TILE_COUNT*Tile.TILE_PIXELS)* Main.getWindow().getPixelSize() >= mx &&
				getYOffset() * Main.getWindow().getPixelSize() <= my && (getYOffset() + TILE_COUNT*Tile.TILE_PIXELS)* Main.getWindow().getPixelSize() >= my;
	}
	
	public void incAll() {
		for (int xt = 0; xt < tiles.length; xt++) {
			for (int yt = 0; yt < tiles.length; yt++) {
				tiles[xt][yt].incrementIndex();
			}
		}
	}
	
	public int getXOffset() {
		return (int) (x * TILE_COUNT * Tile.TILE_PIXELS - stage.getCamera().x*Main.getWindow().getPixelSize());
	}
	
	public int getYOffset() {
		return (int) (y * TILE_COUNT * Tile.TILE_PIXELS - stage.getCamera().y*Main.getWindow().getPixelSize());
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isOnScreen()) {
//			System.out.print("D");
			for (int xt = 0; xt < tiles.length; xt++) {
				for (int yt = 0; yt < tiles.length; yt++) {
					tiles[xt][yt].draw(g);
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
