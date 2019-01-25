package de.jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Image {
	
	public static final String[] IMAGE_EXTENSIONS = {".png", ".jpg" };
	
	static enum TYPE {
		single,
		multi,
		animation
	}
	
	protected TYPE type;
	protected int w, h;
	
	
	public Image(TYPE type) {

	}
	
	public abstract BufferedImage get();
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public TYPE getType(){
		return type;
	}
	
	public static boolean isValidImage(String path) {
		for(String s : IMAGE_EXTENSIONS) {
			if(path.endsWith(s))
				return true;
		}
		return false;
	}
	
}
