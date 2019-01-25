package de.jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Animation extends Image {
	
	protected ArrayList<ImageData> data;
	protected int seed;
	
	public Animation(String path) {
		super(TYPE.animation);
		data = new ArrayList<>();
//		loadImagesRecursively(path);
	}
	
	public static void loadImagesRecursively(String path, ArrayList<ImageData> to) {
		File dir = new File(path);
		File[] expanded = dir.listFiles();
		for(File f : expanded) {
			if(f.isDirectory()) {
				loadImagesRecursively(f.getPath(), to);
			} else {
				if(Image.isValidImage(f.getName()))
					to.add(new ImageData(f.getPath()));
			}
		}
	}
	
	public BufferedImage get() {
		return null;
	}

}
