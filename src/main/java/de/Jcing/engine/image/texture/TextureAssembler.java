package de.jcing.engine.image.texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.jcing.engine.image.ImageData;
import de.jcing.engine.image.ImageFile;

public class TextureAssembler {

	private ArrayList<ImageFile> images;
	private HashMap<Integer, Integer> animationLengths;
	private ArrayList<AtlasCallback> callbacks;

	private TextureAtlas atlas;
	private boolean initialized;

	public TextureAssembler() {
		images = new ArrayList<>();
		animationLengths = new HashMap<>();
		callbacks = new ArrayList<>();
		initialized = false;
	}

	public int addFrame(ImageFile img) {
		int index = images.size();
		images.add(img);
		return index;
	}

	public int addFrames(List<ImageFile> imgs) {
		int index = images.size();
		images.addAll(imgs);
		animationLengths.put(index, imgs.size());
		return index;
	}

	public void addCallback(AtlasCallback callback) {
		callbacks.add(callback);
	}

	public void buildAtlas() {
		ImageData[] imgs = new ImageData[images.size()];
		for (int i = 0; i < images.size(); i++) {
			imgs[i] = images.get(i).getBufferedImage();
		}
		atlas = new TextureAtlas(imgs);
		initialized = true;

		for (AtlasCallback c : callbacks) {
			c.built(this);
		}
	}

	public Image getImage(int id) throws RuntimeException {
		if (!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return new Image(atlas, id);
	}

	public Animation getAnimation(int id) throws RuntimeException {
		if (!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return new Animation(atlas, id, animationLengths.get(id));
	}

	public TextureAtlas getAtlas() throws RuntimeException {
		if (!initialized)
			throw new RuntimeException("textures not yet assembled!");
		return atlas;
	}

	public int size() {
		return images.size();
	}

}
