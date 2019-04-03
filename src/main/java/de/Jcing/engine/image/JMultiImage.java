package de.jcing.engine.image;

import java.io.File;
import java.util.LinkedList;

import de.jcing.Main;
import de.jcing.utillities.log.Log;

public class JMultiImage extends JImage {
	
	private static Log log = new Log(JMultiImage.class).setLogLevel(Log.LOG_LEVEL.error);
	
	
	public JMultiImage(String...path) {
		super();
		for(String s : path)
			loadImagesRecursively(Main.RESSOURCES + s, content);
	}
	
	public static void loadImagesRecursively(String path, LinkedList<JImageData> destination) {
		File dir = new File(path);
		File[] expanded = dir.listFiles();
		log.debug("loading images recursively from: " + dir.getPath());
		int counter = 0;
		for(File f : expanded) {
			if(f.isDirectory()) {
				loadImagesRecursively(f.getPath(), destination);
			} else if(JImage.isValidImage(f.getName())) {
				destination.add(new JImageData(f.getPath()));
				counter++;
			}
		}
		log.debug("added " + counter + " images for a total of " + destination.size());
	}	
}