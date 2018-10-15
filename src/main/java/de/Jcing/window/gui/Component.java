package de.Jcing.window.gui;

import java.awt.Graphics2D;
import java.util.LinkedList;

import de.Jcing.Main;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.engine.io.Mouse;
import de.Jcing.geometry.Rectangle;

public abstract class Component implements Drawable {
	
	protected boolean visible;
	protected Rectangle bounds;
	
	protected Container parent;
	
	protected boolean hovered;
	protected boolean press;
		
	protected LinkedList<Runnable> onClick;
	private boolean handleMouse;

	public Component(int x, int y, int w, int h) {
		bounds = new Rectangle(x,y,w,h);
		parent = null;
		onClick = new LinkedList<>();
	}
	
	public Component(int x, int y) {
		bounds = new Rectangle(x, y, 0, 0);
	}
	
	protected void setParent(Container parent) {
		this.parent = parent;
	}
	
	protected void mouseMove() {
		if(handleMouse) {
			hovered = bounds.contains(Main.getWindow().getMouseOnCanvas());
		}
	}
	
	
	
	protected void mouseClick() {
		if(handleMouse) {
			if(hovered) {
				if(Mouse.keys.get(Mouse.LEFT)) {
					press = true;
				} else if(press) {
					press = false;
					click();
				}
			} else if(press && !Mouse.keys.get(Mouse.LEFT)) {
				press = false;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.translate(bounds.getX(), bounds.getY());
		paint(g);
		g.translate(-bounds.getX(), -bounds.getY());
	}

	protected abstract void paint(Graphics2D g);

	protected void click() {
		for(Runnable r : onClick)
			r.run();
	}
	
	public void listenOnMouse() {
		handleMouse = true;
		if(parent == null) {
			Mouse.addBinding(Mouse.ONMOVE, (i) -> mouseMove());
			Mouse.addBinding(Mouse.ONPRESS, (i) -> mouseClick());
			Mouse.addBinding(Mouse.ONRELEASE, (i) -> mouseClick());
		}
	}
	
	public void setWidth(int w) {
		bounds.width = w;
	}
	
	public void setHeight(int h) {
		bounds.height = h;
	}
		
	public void setSize(int w, int h) {
		bounds.width = w;
		bounds.height = h;
	}

	public LinkedList<Runnable> getOnClick(){
		return onClick;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setPosition(double x, double y) {
		bounds.x = x;
		bounds.y = y;
	}
	
	public int getWidth() {
		return bounds.getWidth();
	}
	
	public int getHeight() {
		return bounds.getHeight();
	}

}
