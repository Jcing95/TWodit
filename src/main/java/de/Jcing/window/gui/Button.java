package de.Jcing.window.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Button extends Canvas {
	
	
	public static final int COLOR_DEFAULT = 0;
	public static final int COLOR_HOVERED = 1;
	public static final int COLOR_PRESSED = 2;
	public static final int COLOR_TEXT = 3;
	
	public static final Color[] DEFAULT_BACK = { new Color(60,60,120), new Color(100,100,160), new Color(40,40,100) };
	
	public static int DEFAULT_PADDING = 2;
	
	protected Label label;
	
	protected int currentCanvas;
	
	protected Color[] colors = {DEFAULT_BACK[0],DEFAULT_BACK[1],DEFAULT_BACK[2]};
	
	protected BufferedImage[] canvases;
	
	
	public Button(String label, int x, int y) {
		super(x,y,0,0);
		this.label = new Label(label, DEFAULT_PADDING, DEFAULT_PADDING);
		canvases = new BufferedImage[3];		
		setSize(DEFAULT_PADDING*2+this.label.getWidth(), DEFAULT_PADDING*2 + this.label.getHeight());
		handleMouse = true;
	}
	
	public void setText(String text) {
		label.setText(text);
		setSize(DEFAULT_PADDING*2+this.label.getWidth(), DEFAULT_PADDING*2 + this.label.getHeight());
		updateCanvasSize();
		updateCanvas();
	}
	
	public void setColor(int index, Color color) {
		colors[index] = color;
		updateCanvas();
	}
	
	public void setColors(Color... colors) {
		int index= 0;
		//first the background colors
		for (; index < this.colors.length && index < colors.length; index++) {
			if(colors[index] != null) {
				this.colors[index] = colors[index];
			};
		}
		//then the text color
		if(colors.length > index)
			label.setColor(colors[index]);
		
		//future colors (maybe borders etc.)
		
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
			g.setColor(colors[i]);
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
			canvas = canvases[COLOR_PRESSED];
		} else if (hovered) {
			canvas = canvases[COLOR_HOVERED];
		} else {
			canvas = canvases[COLOR_DEFAULT];
		}
		super.draw(g);	
	}

	public void setBackground(Color color, int index) {
		this.colors[index] = color;
		updateCanvas();
	}
	
	public void setTextColor(Color color) {
		label.setColor(color);
		updateCanvas();
	}
	
	

}
