package de.Jcing.window.gui;

import java.awt.Graphics2D;

import de.Jcing.image.Image;

public class ImageView extends Component {
	
	Image img;
	
	public ImageView(Image img, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		//TODO: make ImageView extends Canvas and do resizing and stuff once.
	}

	@Override
	protected void paint(Graphics2D g) {
		g.drawImage(img.get().get(), 0, 0, bounds.getWidth(), bounds.getHeight(), null);
	}

}
