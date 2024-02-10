package de.jcing.engine.image;

import java.io.IOException;

public class TextureAtlas extends Texture {

	private final int numTexturesHorizontal;
	private final int numTexturesVertical;
	private final int numTexturesTotal;

	public TextureAtlas(ImageData imageData, int numTexturesHorizontal, int numTexturesVertical, int numTexturesTotal)
			throws IOException {
		super(imageData);
		this.numTexturesHorizontal = numTexturesHorizontal;
		this.numTexturesVertical = numTexturesVertical;
		this.numTexturesTotal = numTexturesTotal;
	}

	public int getSubTextureTotalCount() {
		return numTexturesTotal;
	}

	public float getTextureWidth() {
		return 1f / numTexturesHorizontal;
	}

	public float getTextureHeight() {
		return 1f / numTexturesVertical;
	}

	public int getNumTexturesHorizontal() {
		return numTexturesHorizontal;
	}

	public int getNumTexturesVertical() {
		return numTexturesVertical;
	}

}
