package de.jcing.engine.image;

import org.joml.Vector2f;

public class Image extends TextureAtlas {

	protected int index;

	public Image(TextureAtlas atlas, int index) {
		super(atlas);
		this.index = index % atlas.getSubTextureTotalCount();
	}

	public float getX() {
		return (float) (index % getSubTexturesPerSide()) / getSubTexturesPerSide();
	}

	public float getY() {
		return (float) (index / getSubTexturesPerSide()) / getSubTexturesPerSide();
	}

	public float getWidth() {
		return 1.0f / getSubTexturesPerSide();
	}

	@Override
	public Vector2f getOffset() {
		return new Vector2f(0, 0);
	}

	public int getIndex() {
		return index;
	}

	public void set(Image image) {
		//TODO: throw exception...
		if (id == image.id)
			index = image.getIndex();
	}

}
