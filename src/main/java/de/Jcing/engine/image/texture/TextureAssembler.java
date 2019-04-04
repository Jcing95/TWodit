package de.jcing.engine.image.texture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jcing.engine.image.JImage;
import de.jcing.engine.image.JImageData;

public class TextureAssembler {
	
	private ArrayList<JImageData> mapping;
	private HashMap<Integer, Integer> animationLengths;
	private HashMap<Integer, AtlasCallback> callbacks;
	
	private TextureAtlas atlas;
	private boolean initialized;
	
	public TextureAssembler() {
		mapping = new ArrayList<>();
		animationLengths = new HashMap<>();
		callbacks = new HashMap<>();
		initialized = false;
	}

	public int addFrame(JImage img, AtlasCallback callback) {
		int index = mapping.size();
		Iterator<JImageData> i = img.iterator();
		mapping.add(i.next());
		if(callback != null) {
			callbacks.put(index, callback);
		}
		return index;
	}
	
	public int addFrames(JImage img, AtlasCallback callback) {
		int index = mapping.size();
		Iterator<JImageData> i = img.iterator();
		while(i.hasNext()) {
			mapping.add(i.next());
		}
		animationLengths.put(index,mapping.size()-index);
		if(callback != null) {
			callbacks.put(index, callback);
		}
		return index;
	}

	public void buildAtlas() {
		BufferedImage[] imgs = new BufferedImage[mapping.size()];
		for (int i = 0; i < mapping.size(); i++) {
			imgs[i] = mapping.get(i).getBufferedImage();
		}
		atlas = new TextureAtlas(imgs);
		for(int i: callbacks.keySet()) {
			callbacks.get(i).built(i, this);
		}
		initialized = true;
	}
	
	public Image getImage(int id) throws RuntimeException {
		if(!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return new Image(atlas,id);
	}
	
	public Animation getAnimation(int id) throws RuntimeException {
		if(!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return new Animation(atlas,id,animationLengths.get(id));
	}
	
	public TextureAtlas getAtlas() throws RuntimeException {
		if(!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return atlas;
	}
	
	public int size() {
		return mapping.size();
	}
	
}
