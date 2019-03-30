package de.jcing.image;

import java.awt.image.BufferedImage;

import de.jcing.Main;

public class SingleImage extends JImage {

	protected JImageData data;
	
	public SingleImage(String path) {
		super(TYPE.single);
		data = new JImageData(Main.RESSOURCES + path);
		w = data.getWidth();
		h = data.getHeight();
		
	}
	
	@Override
	public BufferedImage get() {
		return data.data;
	}
	
	
}
