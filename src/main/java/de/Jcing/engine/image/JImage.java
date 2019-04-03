package de.jcing.engine.image;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class JImage {
	
	public static final String[] IMAGE_EXTENSIONS = { ".png", ".jpg" };
	
	protected LinkedList<JImageData> content;
	
	protected int w, h;
	
	
	public JImage() {
		content = new LinkedList<>();
	}
		
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public static boolean isValidImage(String path) {
		for(String s : IMAGE_EXTENSIONS) {
			if(path.endsWith(s))
				return true;
		}
		return false;
	}
	
	public Iterator<JImageData> iterator(){
		return content.iterator();
	}
	
}
