package de.Jcing.window.gui.animator;

import de.Jcing.util.Util;
import de.Jcing.window.gui.Component;
import de.Jcing.window.gui.utillities.Group;

public class Move extends Animator {

	public static final int LEFT = -11;
	public static final int RIGHT = 11;
	public static final int UP = -22;
	public static final int DOWN = 22;

	protected int pixels;
	protected int direction;
	
	protected double moved;

	public Move(Group group, int direction, int pixels) {
		super(group);
		this.pixels = pixels;
		this.direction = direction;
		moved = 0;
	}

	public Move reverse() {
		direction = -direction;
		moved = -moved;
		return this;
	}

	@Override
	protected void animate(Component c, int tick, double of) {
//		System.out.println("anim " + tick + " / " + of + " @ " + pixels + "px (" + pixels/of + ") per");
		switch (direction) {
		case LEFT:
			moved += pixels / of;
			if(moved < pixels)
				c.getBounds().x -= pixels / of;
			break;
		case RIGHT:
			moved += pixels / of;
			if(moved < pixels)
				c.getBounds().x += pixels / of;
			break;
		case UP:
			System.out.println("want up: " + moved);
			moved += pixels / of;
			if(moved < pixels)
				c.getBounds().y -= pixels / of;
			break;
		case DOWN:
			System.out.println("want down: " + moved);
			moved += pixels / of;
			if(moved < pixels)
				c.getBounds().y += pixels / of;
			break;
		}
	}

}
