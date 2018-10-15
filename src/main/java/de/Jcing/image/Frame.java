package de.Jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Frame {
	
	BufferedImage img;
	
	private static ArrayList<File> loadedImages = new ArrayList<>();
	private static HashMap<Integer, BufferedImage> frames = new HashMap<>();
	
	public final int ID;
	public final File path;
	
	public Frame(File file) {
		path = file;
		if(loadedImages.contains(file)) {
			ID = loadedImages.indexOf(file);
			return;
		}
		ID = loadedImages.size();
		loadedImages.add(path);
	}
	
	public BufferedImage get() {
		if(!frames.containsKey(ID)) {
			frames.put(ID, ImageUtils.loadImage(path));
		}
		return frames.get(ID);
	}
	
	public int getID() {
		return ID;
	}
	
	@Override
	public boolean equals(Object o) {
		return ((Frame)o).path.equals(path);
	}
	
	
}
