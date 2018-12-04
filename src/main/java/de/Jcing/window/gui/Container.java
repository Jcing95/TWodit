package de.Jcing.window.gui;

import java.awt.Graphics2D;
import java.util.LinkedList;

import de.Jcing.engine.io.Mouse;
import de.Jcing.util.Point;

public class Container extends Component {
	
	protected LinkedList<Component> subComponents;
	protected boolean unhovered;
	
	public Container(int x, int y, int w, int h) {
		super(x,y,w,h);		
		subComponents = new LinkedList<>();
		unhovered = false;
	}
	
	public void addComponent(Component c) {
		synchronized(this) {
			subComponents.add(c);
			c.setParent(this);
		}
	}
	
	@Override
	public void paint(Graphics2D g) {
		synchronized(this) {
			for (Component c: subComponents)
				c.draw(g);
		}
	}

	protected void mouseMove(Point translatedMouse) {
		hovered = bounds.contains(translatedMouse);
		if(hovered) {
			unhovered = false;
			for(Component c : subComponents) {
				c.mouseMove(translatedMouse.translate(bounds.getOrigin().invert()));
			}
		} else {
			if(!unhovered) {
				unhovered = true;
				for(Component c : subComponents) {
					c.hovered = false;
				}
			}
		}
	}
	
	protected void mouseClick() {
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
		
		for(Component c : subComponents) {
			c.mouseClick();
		}
	}
}
