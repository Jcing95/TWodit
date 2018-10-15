package de.Jcing.window.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Button extends Canvas {
	
	
	public static final int DEFAULT = 0;
	public static final int HOVERED = 1;
	public static final int PRESSED = 2;
	
	public static final Color[] DEFAULT_BACK = { new Color(60,60,120), new Color(100,100,160), new Color(40,40,100) };
	
	public static int DEFAULT_PADDING = 2;
	
	protected Label label;
	
	protected int currentCanvas;
	
	protected Color[] background = {DEFAULT_BACK[0],DEFAULT_BACK[1],DEFAULT_BACK[2]};
	
	protected BufferedImage[] canvases;
	
	
	public Button(String label, int x, int y) {
		super(x,y,0,0);
		this.label = new Label(label, DEFAULT_PADDING, DEFAULT_PADDING);
		canvases = new BufferedImage[3];		
		setSize(DEFAULT_PADDING*2+this.label.getWidth(), DEFAULT_PADDING*2 + this.label.getHeight());
	}
	
	public void setText(String text) {
		label.setText(text);
		setSize(DEFAULT_PADDING*2+this.label.getWidth(), DEFAULT_PADDING*2 + this.label.getHeight());
		updateCanvasSize();
		updateCanvas();
	}
	
	@Override
	public void setSize(int w, int h) {
//		((Component)this).setSize(w, h);
		bounds.width = w;
		bounds.height = h;
		updateCanvasSize();
		updateCanvas();
	}
	
	protected void updateCanvas() {
		for (int i = 0; i < canvases.length; i++) {
			Graphics2D g = canvases[i].createGraphics();
			g.setColor(background[i]);
			g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
//			g.translate(DEFAULT_PADDING, DEFAULT_PADDING);
			label.draw(g);
			g.dispose();
		}
	}
	
	
	protected void updateCanvasSize() {
		for (int i = 0; i < canvases.length; i++) {
			canvases[i] = new BufferedImage(bounds.getWidth(),bounds.getHeight(),BufferedImage.TYPE_INT_ARGB);
		}
		canvas = canvases[currentCanvas];
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(press) {
			canvas = canvases[PRESSED];
		} else if (hovered) {
			canvas = canvases[HOVERED];
		} else {
			canvas = canvases[DEFAULT];
		}
		super.draw(g);	
	}

	public void setBackground(Color color, int index) {
		this.background[index] = color;
		updateCanvas();
	}
	
	public void setTextColor(Color color) {
		label.setColor(color);
		updateCanvas();
	}
	
	

}
