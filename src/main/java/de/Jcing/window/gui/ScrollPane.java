package de.Jcing.window.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import de.Jcing.engine.io.Binding;
import de.Jcing.engine.io.Mouse;
import de.Jcing.util.Point;
import de.Jcing.util.Util;
import de.jcing.utillities.task.Task;
import de.jcing.utillities.task.Topic;

public class ScrollPane extends Container {
	
	private static final Topic SCROLLTOPIC = new Topic("Scrollpanes");
	
	protected Canvas canvas;
	
	protected double xScroll, yScroll;
	
	protected double yVel;
	
	protected Color background;

	public ScrollPane(int x, int y, int w, int h) {
		super(x,y,w,h);
		canvas = new Canvas(0,0,w,h);
		background = new Color(0,0,0,0);
		//TODO: implement scrollbar
	}
	
	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2 = canvas.getGraphics();
		g2.setBackground(background);
		g2.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
		g2.translate(xScroll, yScroll);
		super.paint(g2);
		g2.dispose();
		canvas.draw(g);
	}
	
	@Override
	protected void mouseMove(Point translatedMouse) {
		hovered = bounds.contains(translatedMouse);
		if(hovered) {
			unhovered = false;
			Point origin = bounds.getOrigin().translate(new Point(xScroll,yScroll)).invert();
			for(Component c : subComponents) {
				c.mouseMove(translatedMouse.translate(origin));
			}
		} else {
			unhovered = true;
			for(Component c : subComponents) {
				c.hovered = false;
			}
		}
	}
	
	@Override
	public void listenOnMouse() {
		super.listenOnMouse();
		bindings.add(Mouse.addBinding(Mouse.ONWHEEL, new Binding() {

			@Override
			public void onAction(int key) {
				if(hovered) {
					yVel -= key*2.5;
					scrollJob.start();
				}
			}
			
		}));
	}
	
	protected Task scrollJob = new Task(() -> {
		yScroll += yVel;
		yVel = yVel*0.75;
		if(Util.fastABS(yVel) < 1){
			yVel = 0;
			finishScrolling();
		}
	}).name("Scrolling").repeat(Task.perSecond(30)).inTopic(SCROLLTOPIC);

	
	protected void finishScrolling() {
		scrollJob.stop();
	}
	
	public ScrollPane setBackground(Color color) {
		background = color;
		return this;
	}
	
	
}
