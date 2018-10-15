package de.Jcing.image;

import java.io.File;
import java.util.ArrayList;

import de.Jcing.tasks.Clock;

public class Image {
	
	private static final String INSTRUCTIONFILE = "image.j";
	private static final float DEFAULTFPS = 10;
	
	private ArrayList<Image> frames;
	Frame frame;
	
	private int frameTime = (int)(1000 / DEFAULTFPS);
	private boolean hasMultiple;
	private boolean isAnimation;
	
	public Image(File f) {
		if(f.isFile()) {
			//load single Image
			//TODO: file no Image error handling here
			frame = new Frame(f);
			return;
		}
		
		if(f.isDirectory()) {
			File[] files = f.listFiles();
			//check Image.j file for scripted instructions.
			for (int i = 0; i < files.length; i++) {
				if(files[i].getName().equals(INSTRUCTIONFILE)) {
					
					//TODO: pass Instruction file to parser and execute.
					return;
				}
			}
			
			//multiple files at this location --> load all sub images recursively.
			if(files.length > 1) {
				hasMultiple = true;
				frames = new ArrayList<>();
				for (int i = 0; i < files.length; i++) {
					frames.add(new Image(files[i]));
				}
			}
			//one directory --> load sub images as Animation
			else { 
				if(files[0].isDirectory()) {
					System.out.println("anim");
					isAnimation = true;
					frames = new ArrayList<>();
					frames.add(new Image(files[0]));
				} else {
					frame = new Frame(files[0]);
				}
			}
			
		}
	}
	
	public Image(String string) {
		this(new File(string));
	}

	public Frame get() {
		return get((int)(Clock.millis() / frameTime));
	}
	
	public Frame get(int index) {
		if(hasMultiple) {
			return frames.get(index % frames.size()).get(index);
		}
		if(isAnimation) {
			return frames.get(index % frames.size()).get(index);
		}
		return frame;
	}
	
	public int size() {
//		System.out.println(hasMultiple);
		return frames.size();
	}
}
