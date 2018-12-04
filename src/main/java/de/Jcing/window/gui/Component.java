package de.Jcing.window.gui;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.LinkedList;

import de.Jcing.Main;
import de.Jcing.engine.graphics.Drawable;
import de.Jcing.engine.io.Binding;
import de.Jcing.engine.io.Mouse;
import de.Jcing.geometry.Rectangle;
import de.Jcing.util.Point;

public abstract class Component implements Drawable {

	protected Container parent;

	protected Rectangle bounds;

	protected LinkedList<Runnable> onClick;
	protected HashSet<Binding> bindings;

	protected boolean visible;

	protected boolean hovered;
	protected boolean press;

	protected boolean verticalCentered;
	protected boolean horizontalCentered;

	protected boolean handleMouse;
	protected boolean isVisible;

	public Component(int x, int y) {
		this(x,y,0,0);
	}

	public Component(int x, int y, int w, int h) {
		bounds = new Rectangle(x, y, w, h);
		bindings = new HashSet<>();
		parent = null;
		onClick = new LinkedList<>();
		isVisible = true;
	}


	protected void mouseMove(Point translatedMouse) {
		if (handleMouse) {
			hovered = bounds.contains(translatedMouse);
		}
	}

	protected void mouseClick() {
		if (handleMouse) {
			if (hovered) {
				if (Mouse.keys.get(Mouse.LEFT)) {
					press = true;
				} else if (press) {
					press = false;
					click();
				}
			} else if (press && !Mouse.keys.get(Mouse.LEFT)) {
				press = false;
			}
		}
	}

	protected abstract void paint(Graphics2D g);

	@Override
	public void draw(Graphics2D g) {
		if (isVisible) {
			g.translate(bounds.getX(), bounds.getY());
			paint(g);
			g.translate(-bounds.getX(), -bounds.getY());
		}
	}


	protected void click() {
		for (Runnable r : onClick)
			r.run();
	}

	public void listenOnMouse() {
		handleMouse = true;
		if (parent == null) {
			bindings.add(Mouse.addBinding(Mouse.ONMOVE, (i) -> mouseMove(Main.getWindow().getMouseOnCanvas())));
			bindings.add(Mouse.addBinding(Mouse.ONPRESS, (i) -> mouseClick()));
			bindings.add(Mouse.addBinding(Mouse.ONRELEASE, (i) -> mouseClick()));
		}
	}

	public void unlisten() {
		handleMouse = false;
		for (Binding b : bindings) {
			Mouse.removeBinding(b);
		}
	}

	public void centerVertical(boolean center) {
		if (center != verticalCentered) {
			if (center)
				bounds.y -= bounds.height / 2;
			else
				bounds.y += bounds.height / 2;
			verticalCentered = center;
		}
	}

	public void centerHorizontal(boolean center) {
		if (center != horizontalCentered) {
			if (center)
				bounds.x -= bounds.width / 2;
			else
				bounds.x += bounds.width / 2;
			horizontalCentered = center;
		}
	}

	public LinkedList<Runnable> getOnClick() {
		return onClick;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	
	public int getWidth() {
		return bounds.getWidth();
	}

	public int getHeight() {
		return bounds.getHeight();
	}
	
	protected void setParent(Container parent) {
		this.parent = parent;
	}	
	
	public void setPosition(double x, double y) {
		bounds.x = x;
		bounds.y = y;
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
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

}
