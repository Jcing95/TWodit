
package de.jcing.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;

import de.jcing.Main;
import de.jcing.engine.Trigger;
import de.jcing.engine.entity.Creature;
import de.jcing.engine.image.JMultiImage;
import de.jcing.geometry.Rectangle;
import de.jcing.util.Point;

@Deprecated
public class Tile {
	
	public static final int TILE_PIXELS = 32;
	
	private LinkedList<JMultiImage> textures;
	private LinkedList<Creature> entities;
	private LinkedList<Trigger> triggers;
	
	private JMultiImage testBack = new JMultiImage("gfx/terrain/grass");
	
	private boolean collision;
	
	private Chunk chunk;
	private int x, y;
		
	public Tile(int x, int y, Chunk chunk) {
		this.chunk = chunk;
		this.x = x;
		this.y = y;
		textures = new LinkedList<>();
		entities = new LinkedList<>();
		triggers = new LinkedList<>();
//		addTexture(testBack,testBack.seed());
	}
	
	public void addTexture(JMultiImage img) {
		//TODO: catch invalid images for Tiles
		textures.add(img);
	}
	
	public void addTexture(JMultiImage img, int index) {
		textures.add(img);
//		((JMultiImage)img).seed(index);
	}
	
	public void popTexture() {
		textures.removeLast();
	}
	
	@Deprecated
	public void incrementIndex() {
		JMultiImage img = textures.getLast();
//		img.seed(img.getSeed()+1);
	}
	
	public void draw(Graphics2D g, Point offset) {
		offset = computePositionOnScreen(offset);
		Iterator<JMultiImage> texIter = textures.iterator();
//		while(texIter.hasNext()) {
//			g.drawImage(texIter.next().get(), offset.getXi(), offset.getYi(), null);
//		}
		if(hovered()) {
			g.setColor(new Color(255,255,255,55));
			g.fillRect(offset.getXi(), offset.getYi(), TILE_PIXELS, TILE_PIXELS);
		}
	}
	
	public boolean hovered() {
		return new Rectangle(getXOnScreen(), getYOnScreen(),
				TILE_PIXELS, TILE_PIXELS)
				.contains(Main.getWindow().getMouseOnCanvas());
	}
	
	public Point computePositionOnScreen(Point offset) {
		return new Point(x * TILE_PIXELS + offset.getXd(),y * TILE_PIXELS + offset.getYd());
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
	
	public LinkedList<Creature> getEntities(){
		return entities;
	}
	
	public Tile enter(Creature e) {
		entities.add(e);
		return this;
	}
	
	public void leave(Creature e) {
		entities.remove(e);
	}
	
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	
	public boolean hasCollision() {
		return collision;
	}
	
	
}
