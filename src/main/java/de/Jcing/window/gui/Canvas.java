package de.Jcing.window.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Canvas extends Component {

	protected BufferedImage canvas;
		
	public Canvas(int x, int y, int w, int h) {
		super(x,y,w,h);
		if(w > 0 && h > 0)
			canvas = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void setWidth(int w) {
		super.setWidth(w);
		canvas = new BufferedImage(w,bounds.getHeight(),BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void setHeight(int h) {
		super.setHeight(h);
		canvas = new BufferedImage(bounds.getWidth(),h,BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		canvas = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void paint(Graphics2D g) {
		g.drawImage(canvas, 0, 0, null);
	}
	
	public Graphics2D getGraphics() {
		return (Graphics2D) canvas.getGraphics();
	}
	
}
