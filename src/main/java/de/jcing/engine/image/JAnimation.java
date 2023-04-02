package de.jcing.engine.image;

import java.io.File;
import java.util.ArrayList;

import de.jcing.Main;
import de.jcing.util.Log;

public class JAnimation extends JImage {

	public static final int DEFAULT_FRAMES_PER_SECOND = 5;

	protected final int fps;

	public JAnimation(String path) {
		super();
		fps = DEFAULT_FRAMES_PER_SECOND;
		loadAnimation(Main.RESSOURCES + path, content);
	}

	public static void loadAnimation(String path, ArrayList<ImageFile> to) {
		File dir = new File(path).getAbsoluteFile();
		Log.info("Loading animation from: " + dir.getAbsolutePath());
		File[] expanded = dir.listFiles();
		for (File f : expanded) {
			if (JImage.isValidImage(f.getName()))
				to.add(new ImageFile(f.getAbsolutePath()));
		}
	}
}
