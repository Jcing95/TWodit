package de.jcing.engine.image;

import java.io.File;
import java.util.LinkedList;

import de.jcing.Main;
import de.jcing.utillities.log.Log;

public class JAnimation extends JImage {
	
	private static final Log log = new Log(JAnimation.class);
	
	public static final int DEFAULT_FRAMES_PER_SECOND = 5;
	
	protected int fps;
	
	public JAnimation(String path) {
		super();
		fps = DEFAULT_FRAMES_PER_SECOND;
		loadAnimation(Main.RESSOURCES + path, content);
	}

	public static void loadAnimation(String path, LinkedList<JImageData> to) {
		File dir = new File(path).getAbsoluteFile();
		log.info("Loading animation from: " + dir.getAbsolutePath());
		File[] expanded = dir.listFiles();
		for(File f : expanded) {
			if(JImage.isValidImage(f.getName()))
				to.add(new JImageData(f.getAbsolutePath()));
		}
	}
}
