package de.jcing.engine.image.texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.jcing.engine.image.ImageData;
import de.jcing.engine.image.JImage;
import de.jcing.engine.image.ImageFile;

public class TextureAssembler {

	private final ArrayList<ImageFile> images;
	private final HashMap<Integer, Integer> animationLengths;
	private final ArrayList<AtlasCallback> callbacks;

	private TextureAtlas atlas;
	private boolean initialized;

	public TextureAssembler() {
		images = new ArrayList<>();
		animationLengths = new HashMap<>();
		callbacks = new ArrayList<>();
		initialized = false;
	}

	public int addFrame(JImage img) {
		int index = images.size();
		Iterator<ImageFile> i = img.iterator();
		images.add(i.next());
		return index;
	}

	public int addFrames(JImage img) {
		int index = images.size();
		Iterator<ImageFile> i = img.iterator();
		while (i.hasNext()) {
			images.add(i.next());
		}
		animationLengths.put(index, images.size() - index);
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
