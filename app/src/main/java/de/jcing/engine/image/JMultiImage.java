package de.jcing.engine.image;

import java.io.File;
import java.util.ArrayList;

import de.jcing.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMultiImage extends JImage {

	private static final Logger LOG = LoggerFactory.getLogger(JMultiImage.class);
	
	public JMultiImage(String... path) {
		super();
		for (String s : path)
			loadImagesRecursively(Main.RESOURCES + s, content);
	}

	public static void loadImagesRecursively(String path, ArrayList<ImageFile> destination) {
		File dir = new File(path);
		File[] expanded = dir.listFiles();
		LOG.debug("loading images recursively from: {}", dir.getPath());
		int counter = 0;
		if(expanded != null){
			for (File f : expanded) {
				if (f.isDirectory()) {
					loadImagesRecursively(f.getPath(), destination);
				} else if (JImage.isValidImage(f.getName())) {
					destination.add(new ImageFile(f.getPath()));
					counter++;
				}
			}
		}
		LOG.debug("added {} images for a total of {}", counter, destination.size());
	}
}
