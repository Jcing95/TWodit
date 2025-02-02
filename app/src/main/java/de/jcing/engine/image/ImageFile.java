package de.jcing.engine.image;

import java.util.HashMap;

public class ImageFile {

	public static final boolean FLIP_IMAGE_VERTICALLY = true;

	protected static final HashMap<String, ImageData> LOADED_IMAGES = new HashMap<>();

	protected String path;

	protected final ImageData data;

	public ImageFile(String path) {
		if (LOADED_IMAGES.containsKey(path)) {
			data = LOADED_IMAGES.get(path);
		} else {
			data = ImageLoader.getInstance().load(path, FLIP_IMAGE_VERTICALLY);
			LOADED_IMAGES.put(path, data);
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

	public ImageData getBufferedImage() {
		return data;
	}
	
}
