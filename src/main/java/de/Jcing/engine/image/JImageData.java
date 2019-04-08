package de.jcing.engine.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.jcing.utillities.log.Log;

public class JImageData {
	
	private static final Log log = new Log(JImageData.class);
	
	public static final boolean FLIP_IMAGE_VERTICALLY = true;
	
	protected static final HashMap<String, BufferedImage> LOADED_IMAGES = new HashMap<>();
	
	protected String path;
	
	protected BufferedImage data;
	
	public JImageData(String path) {
		if(LOADED_IMAGES.containsKey(path)) {
			data = LOADED_IMAGES.get(path);
		} else {
			try {
				data = ImageIO.read(new File(path));
				if(FLIP_IMAGE_VERTICALLY)
					flip(data);
				LOADED_IMAGES.put(path, data);
			} catch (IOException e) {
				log.error("could not load: " + path);
				e.printStackTrace();
			}
		}
	}
	
	public static void flip(BufferedImage img) {
		int[] lineBuffer = new int[img.getWidth()];
		int[] lineBuffer2 = new int[img.getWidth()];
		for(int y = 0; y < img.getHeight()/2; y++) {
			img.getRGB(0, y, img.getWidth(), 1, lineBuffer, 0, 1);
			img.getRGB(0, img.getHeight()-y-1, img.getWidth(), 1, lineBuffer2, 0, 1);	
			img.setRGB(0, y, img.getWidth(), 1, lineBuffer2, 0, 1);
			img.setRGB(0, img.getHeight()-y-1, img.getWidth(), 1, lineBuffer, 0, 1);
		}
	}
	
	public int getID() {
		return path.hashCode();
	}
	
	public int getWidth() {
		return data.getWidth();
	}
	
	public int getHeight() {
		return data.getHeight();
	}

	public BufferedImage getBufferedImage() {
		return data;
	}
}
