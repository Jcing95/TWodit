package de.Jcing.engine.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.LinkedList;

import de.Jcing.Main;
import de.Jcing.engine.Trigger;
import de.Jcing.engine.entity.Entity;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.geometry.Rectangle;
import de.Jcing.image.Image;

public class Tile implements Drawable {
	
	public static final int TILE_PIXELS = 32;
	
	private LinkedList<Image> textures;
	private LinkedList<Entity> entities;
	private LinkedList<Trigger> triggers;
	
	private Image testBack = new Image(new File(Main.RESSOURCES+"gfx/terrain/grass"));
	
	private boolean collision;
	
	private Chunk chunk;
	private int x, y;
	
	private int texIndex;
	
	
	public Tile(int x, int y, Chunk chunk) {
		this.chunk = chunk;
		this.x = x;
		this.y = y;
		textures = new LinkedList<>();
		entities = new LinkedList<>();
		triggers = new LinkedList<>();
		texIndex = (int)(Math.random()*1000);//Generator.getValue(chunk.getX()*Chunk.TILE_COUNT+x, chunk.getY()+Chunk.TILE_COUNT+y);
		textures.add(testBack);
//		System.out.println(texIndex);
	}
	
	public void addTexture(Image img) {
		//TODO: catch invalid images for Tiles
		textures.add(img);
	}
	
	public void popTexture() {
		textures.removeLast();
	}
	
	

	@Override
	public void draw(Graphics2D g) {
		int xOff = getXOnScreen();
		int yOff = getYOnScreen();
		if(hovered()) {
			g.setColor(Color.white);
			g.fillRect(xOff, yOff, TILE_PIXELS, TILE_PIXELS);
		}
		if(!hovered())
		for(Image i : textures) {
			g.drawImage(i.get(texIndex).get(), xOff, yOff, null);
		}
	}
	
	public boolean hovered() {
		return new Rectangle(getXOnScreen(), getYOnScreen(),
				TILE_PIXELS, TILE_PIXELS)
				.contains(Main.getWindow().getMouseOnCanvas());
	}
	
	public int getXOnScreen() {
		return x * TILE_PIXELS + chunk.getXOffset();
	}
	
	public int getYOnScreen() {
		return y * TILE_PIXELS + chunk.getYOffset();
	}
	
	public int getWorldX() {
		return x * chunk.getX()*Chunk.TILE_COUNT;
	}
	
	public int getWorldY() {
		return y * chunk.getY()*Chunk.TILE_COUNT;
	}
	
	public void removeFrame(boolean last) {
		if(last)
			textures.removeLast();
		else
			textures.removeFirst();
	}
	
	public LinkedList<Entity> getEntities(){
		return entities;
	}
	
	public Tile enter(Entity e) {
		entities.add(e);
		return this;
	}
	
	public void leave(Entity e) {
		entities.remove(e);
	}
	
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	
	public boolean hasCollision() {
		return collision;
	}
	
	
}
