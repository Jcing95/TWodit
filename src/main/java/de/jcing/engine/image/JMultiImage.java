package de.jcing.engine.image;

import java.io.File;
import java.util.ArrayList;

import de.jcing.Main;
import de.jcing.util.Log;
import de.jcing.util.Log.Logger;

public class JMultiImage extends JImage {

	private static final Logger log = Log.getInstance().setLevel(Log.LEVEL.error);
	
	public JMultiImage(String... path) {
		super();
		for (String s : path)
			loadImagesRecursively(Main.RESSOURCES + s, content);
	}

	public static void loadImagesRecursively(String path, ArrayList<ImageFile> destination) {
		File dir = new File(path);
		File[] expanded = dir.listFiles();
		log.debug("loading images recursively from: " + dir.getPath());
		int counter = 0;
		for (File f : expanded) {
			if (f.isDirectory()) {
				loadImagesRecursively(f.getPath(), destination);
			} else if (JImage.isValidImage(f.getName())) {
				destination.add(new ImageFile(f.getPath()));
				counter++;
			}
		}
		log.debug("added " + counter + " images for a total of " + destination.size());
	}
}
