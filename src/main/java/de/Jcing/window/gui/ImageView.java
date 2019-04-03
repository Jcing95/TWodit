package de.jcing.window.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.jcing.engine.image.JSingleImage;


public class ImageView extends Canvas {
	
	JSingleImage img;
	
	public ImageView(JSingleImage img, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		Graphics2D g = getGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img.get(), 0, 0, w, h, null);
		g.dispose();
		//TODO: add more ImageView capabilities;
	}

}
