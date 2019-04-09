package de.jcing.window.gui.animator;

import de.jcing.engine.gui.Component;
import de.jcing.window.gui.utillities.Group;

public class Mover extends Animator {

	public static final int LEFT = -11;
	public static final int RIGHT = 11;
	public static final int UP = -22;
	public static final int DOWN = 22;

	protected int pixels;
	protected int direction;
	
	protected double moved;

	public Mover(Group group, int direction, int pixels) {
		super(group);
		this.pixels = pixels;
		this.direction = direction;
		moved = 0;
	}

	public Mover reverse() {
		direction = -direction;
		moved = (pixels - moved) % pixels;
		return this;
	}

	@Override
	protected void animate(Component c, int tick, double of) {
		switch (direction) {
		case LEFT:
			if(moved < pixels) {
				moved += pixels / of;
				c.getBounds().x -= pixels / of;
			}
			break;
		case RIGHT:
			if(moved < pixels) {
				moved += pixels / of;
				c.getBounds().x += pixels / of;
			}
			break;
		case UP:
			if(moved < pixels) {
				moved += pixels / of;
				c.getBounds().y -= pixels / of;
			}
			break;
		case DOWN:
			if(moved < pixels) {
			moved += pixels / of;
				c.getBounds().y += pixels / of;
			}
			break;
		}
	}

}
