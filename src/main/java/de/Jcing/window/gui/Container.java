package de.Jcing.window.gui;

import java.awt.Graphics2D;
import java.util.HashSet;

import de.Jcing.Main;
import de.Jcing.engine.io.Mouse;

public class Container extends Component {
	
	protected HashSet<Component> subComponents;
	
	public Container(int x, int y, int w, int h) {
		super(x,y,w,h);		

	}
	
	public void addComponent(Component c) {
		subComponents.add(c);
		c.setParent(this);
	}
	
	@Override
	public void paint(Graphics2D g) {
		for (Component c: subComponents)
			c.draw(g);
	}

	protected void mouseMove() {
		hovered = bounds.contains(Main.getWindow().getMouseOnCanvas());
		for(Component c : subComponents) {
			c.mouseMove();
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
