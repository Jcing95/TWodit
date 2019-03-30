package de.jcing.image;

import java.awt.image.BufferedImage;

public abstract class JImage {
	
	public static final String[] IMAGE_EXTENSIONS = { ".png", ".jpg" };
	
	static enum TYPE {
		single,
		multi,
		animation
	}
	
	protected TYPE type;
	protected int w, h;
	
	
	public JImage(TYPE type) {
		this.type = type;
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
