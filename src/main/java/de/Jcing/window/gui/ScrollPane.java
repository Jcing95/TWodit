package de.Jcing.window.gui;

import java.awt.Graphics2D;

import de.Jcing.engine.io.Binding;
import de.Jcing.engine.io.Mouse;

public class ScrollPane extends Container {
	
	protected Canvas canvas;
	
	protected int xScroll, yScroll;
	
	
	public ScrollPane(int x, int y, int w, int h) {
		super(x,y,w,h);
		canvas = new Canvas(x,y,w,h);
	}
	
	@Override
	public void paint(Graphics2D g) {
		//TODO: efficiency tweak ScrollPane!
		//only repaint on certain events - scroll, hover etc.
		Graphics2D g2 = canvas.getGraphics();
		g2.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
		g2.translate(xScroll, yScroll);
		super.paint(g2);
		g2.dispose();
		canvas.draw(g);
	}
	
	//TODO: implement scrolling + scrollbar
	@Override
	public void listenOnMouse() {
		super.listenOnMouse();
		bindings.add(Mouse.addBinding(Mouse.ONWHEEL, new Binding() {

			@Override
			public void onAction(int key) {
				if(hovered) {
					yScroll += key*2;
				}
			}
			
		}));
	}
	
}
