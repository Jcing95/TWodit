package de.jcing.engine.image;

import java.io.File;
import java.util.ArrayList;

import de.jcing.engine.image.exceptions.NotInitializedException;
import de.jcing.utillities.log.Log;

public abstract class ImageLoader {

	public static String[] VALID_IMAGE_EXTENSIONS = { ".png" };
	private static ImageLoader loader;

	public static ImageLoader getInstance() {
		if (loader == null)
			throw new NotInitializedException("The ImageLoader is not yet initialized! use init() to initialize before use");
		return loader;
	}

	public abstract ImageData load(String path, boolean flip);

	public static boolean validateImageExtension(String name) {
		for (String s : VALID_IMAGE_EXTENSIONS) {
			if (name.endsWith(s))
				return true;
		}
		return false;
	}

	public static void init(ImageLoader imageLoader) {
		loader = imageLoader;
	}

	public static void loadAnimation(String path, ArrayList<ImageFile> to) {
		File dir = new File(path).getAbsoluteFile();
		Log.info("Loading animation from: " + dir.getAbsolutePath());
		File[] expanded = dir.listFiles();
		for (File f : expanded) {
			if (validateImageExtension(f.getName()))
				to.add(new ImageFile(f.getAbsolutePath()));
		}
	}

	public static void loadImagesRecursively(String path, ArrayList<ImageFile> destination) {
		File dir = new File(path);
		File[] expanded = dir.listFiles();
		Log.debug("loading images recursively from: " + dir.getPath());
		int counter = 0;
		for (File f : expanded) {
			if (f.isDirectory()) {
				loadImagesRecursively(f.getPath(), destination);
			} else if (validateImageExtension(f.getName())) {
				destination.add(new ImageFile(f.getPath()));
				counter++;
			}
		}
		Log.debug("added " + counter + " images for a total of " + destination.size());
	}

}
