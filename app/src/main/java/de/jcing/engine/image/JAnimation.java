package de.jcing.engine.image;

import java.io.File;
import java.util.ArrayList;

import de.jcing.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAnimation extends JImage {
	private static final Logger LOG = LoggerFactory.getLogger(JAnimation.class);

	public static final int DEFAULT_FRAMES_PER_SECOND = 5;

	protected final int fps;

	public JAnimation(String path) {
		super();
		fps = DEFAULT_FRAMES_PER_SECOND;
		loadAnimation(Main.RESOURCES + path, content);
	}

	public static void loadAnimation(String path, ArrayList<ImageFile> to) {
		File dir = new File(path).getAbsoluteFile();
		LOG.info("Loading animation from: " + dir.getAbsolutePath());
		File[] expanded = dir.listFiles();
		if(expanded == null) return;
		for (File f : expanded) {
			if (JImage.isValidImage(f.getName()))
				to.add(new ImageFile(f.getAbsolutePath()));
		}
	}
}
