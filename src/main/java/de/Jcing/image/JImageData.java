package de.jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.jcing.utillities.log.Log;

public class JImageData {
	
	private static final Log log = new Log(JImageData.class);
	
	protected static final HashMap<String, BufferedImage> LOADED_IMAGES = new HashMap<>();
	
	protected String path;
	
	protected BufferedImage data;
	
	public JImageData(String path) {
		if(LOADED_IMAGES.containsKey(path)) {
			data = LOADED_IMAGES.get(path);
		} else {
			try {
				data = ImageIO.read(new File(path));
				LOADED_IMAGES.put(path, data);
			} catch (IOException e) {
				log.error("could not load: " + path);
				e.printStackTrace();
			}
		}
	}
	
	public int getWidth() {
		return data.getWidth();
	}
	
	public int getHeight() {
		return data.getHeight();
	}
}
