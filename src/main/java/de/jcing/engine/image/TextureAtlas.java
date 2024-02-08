package de.jcing.engine.image;

import org.joml.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextureAtlas extends Texture {

	private static final Logger LOG = LoggerFactory.getLogger(TextureAtlas.class);
	private final int subTextureTotalCount;
	private final int subTexturesPerSide;
	private final int subTextureSideLength;


	protected TextureAtlas(TextureAtlas tex) {
		super(tex);
		this.subTextureSideLength = tex.subTextureSideLength;
		this.subTexturesPerSide = tex.subTexturesPerSide;
		this.subTextureTotalCount = tex.subTextureTotalCount;
	}

	@Override
	public Vector2f getOffset() {
		return new Vector2f(0.2f, 1f);
	}

	public int getSubTextureTotalCount() {
		return subTextureTotalCount;
	}

	public int getSubTexturesPerSide() {
		return subTexturesPerSide;
	}

}
