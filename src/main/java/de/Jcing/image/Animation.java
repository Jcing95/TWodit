package de.jcing.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import de.jcing.Main;
import de.jcing.utillities.task.Task;

public class Animation extends Image {
	

	public static final int DEFAULT_FRAMES_PER_SECOND = 5;
	
	protected ArrayList<ImageData> data;
	protected int seed;
	protected int fps;
	
	public Animation(String path) {
		super(TYPE.animation);
		data = new ArrayList<>();
		fps = DEFAULT_FRAMES_PER_SECOND;
		loadAnimation(Main.RESSOURCES + path, data);
	}
	
	public BufferedImage get() {
		int index = (int)(Task.millis()/(1000.0/fps));
		return data.get(index % (data.size())).data;
	}
	
	public BufferedImage get(int index) {
		return data.get(index % (data.size())).data;
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}

	public static void loadAnimation(String path, ArrayList<ImageData> to) {
		File dir = new File(path).getAbsoluteFile();
		File[] expanded = dir.listFiles();
		for(File f : expanded) {
			if(Image.isValidImage(f.getName()))
				to.add(new ImageData(f.getAbsolutePath()));
		}
	}

}
