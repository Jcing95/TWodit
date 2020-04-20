package de.jcing.engine.image;

import de.jcing.Main;

public class JSingleImage extends JImage {

	public JSingleImage(String path) {
		super();
		ImageFile data = new ImageFile(Main.RESSOURCES + path);
		w = data.getWidth();
		h = data.getHeight();
		content.add(data);

	}

}
