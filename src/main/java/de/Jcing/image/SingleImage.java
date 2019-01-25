package de.jcing.image;

import java.awt.image.BufferedImage;

public class SingleImage extends Image {

	protected ImageData data;
	
	public SingleImage(String path) {
		super(TYPE.single);
		data = new ImageData(path);
		w = data.getWidth();
		h = data.getHeight();
		
	}
	
	@Override
	public BufferedImage get() {
		return data.data;
	}
	
	
}
