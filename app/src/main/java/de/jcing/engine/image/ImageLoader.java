package de.jcing.engine.image;

import de.jcing.engine.image.exceptions.NotInitializedException;

public abstract class ImageLoader {
	
	private static ImageLoader loader;
	
	public static ImageLoader getInstance() {
		if(loader == null)
			throw new NotInitializedException("The ImageLoader is not yet initialized! use init() to initialize before use");
		return loader;
	}
	
	public abstract ImageData load(String path, boolean flip);

	public static void init(ImageLoader imageLoader) {
		loader = imageLoader;
	}
	
	
	
}
