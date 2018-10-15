package de.Jcing.window.gui;

import java.awt.Graphics2D;

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
	
	
	
	
}
