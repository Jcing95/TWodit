package de.Jcing.engine.world;

import java.awt.Graphics2D;

import de.Jcing.Main;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.window.Window;

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
	
	public void load(boolean loaded) {
		//TODO: handle chunkloading logic
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
		if(isOnScreen())
		for (int xt = 0; xt < tiles.length; xt++) {
			for (int yt = 0; yt < tiles.length; yt++) {
				tiles[xt][yt].draw(g);
			}
		}
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
}
