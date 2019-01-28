package de.jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import de.jcing.Main;
import de.jcing.utillities.log.Log;

public class MultiImage extends Image{
	
	
	private static Log log = new Log(MultiImage.class);
	
	protected static final Random random = new Random(1337);
	protected ArrayList<ImageData> data;
	protected int seed;
	
	public MultiImage(String...path) {
		super(TYPE.multi);
		data = new ArrayList<>();
		for(String s : path)
			loadImagesRecursively(Main.RESSOURCES + s, data);
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
	
	public int seed() {
		seed = Math.abs(random.nextInt());
		return seed;
	}
	
	public void seed(int seed) {
		this.seed = seed;
	}
	
	@Override
	public BufferedImage get() {
		return data.get(seed%(data.size()-1)).data;
	}
	
	
}