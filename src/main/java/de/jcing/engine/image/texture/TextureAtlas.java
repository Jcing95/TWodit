package de.jcing.engine.image.texture;

import org.joml.Vector2f;

import de.jcing.engine.image.ImageData;
import de.jcing.util.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextureAtlas extends Texture {

	private static final Logger LOG = LoggerFactory.getLogger(TextureAtlas.class);
	private final int subTextureTotalCount;
	private final int subTexturesPerSide;
	private final int subTextureSideLength;

	public TextureAtlas(String path, int subTextureSideLength, int subTexturesPerSide) throws Exception {
		super(path);
		subTextureTotalCount = subTexturesPerSide * subTexturesPerSide;
		this.subTexturesPerSide = subTexturesPerSide;
		this.subTextureSideLength = subTextureSideLength;
	}

	protected TextureAtlas(TextureAtlas tex) {
		super(tex);
		this.subTextureSideLength = tex.subTextureSideLength;
		this.subTexturesPerSide = tex.subTexturesPerSide;
		this.subTextureTotalCount = tex.subTextureTotalCount;
	}

	public TextureAtlas(ImageData... subImages) {
		super(combineSubImages(subImages));
		subTextureTotalCount = subImages.length;
		subTexturesPerSide = Maths.roundUp(Math.sqrt(subTextureTotalCount));
		subTextureSideLength = subImages[0].getWidth();
	}

	@Override
	public Vector2f getOffset() {
		return new Vector2f(0.2f, 1f);
	}

	private static ImageData combineSubImages(ImageData... subImages) {
		int count = subImages.length;
		if (assertSameSize(subImages)) {
			int sides = Maths.roundUp(Math.sqrt(count));
			int size = subImages[0].getWidth() * sides;
			LOG.debug("combining {} images to atlas with {} textures per side and a size of {}px", count, sides, count);
			ImageData combined = new ImageData(size, size);
			int x = 0;
			int y = 0;
			for (ImageData subImage : subImages) {
				combined.copy(subImage, x, y);
				x += subImages[0].getWidth();
				if (x >= size) {
					x = 0;
					y += subImages[0].getWidth();
				}
			}
			return combined;
		}
		return null;
	}

	public static boolean assertSameSize(ImageData... bufferedImages) {
		int w = bufferedImages[0].getWidth();
		for (ImageData img : bufferedImages) {
			if (img.getWidth() != img.getHeight() || img.getWidth() != w)
				return false;
		}
		return true;
	}

	public int getSubTextureTotalCount() {
		return subTextureTotalCount;
	}

	public int getSubTextureSideLength() {
		return subTextureSideLength;
	}

	public int getSubTexturesPerSide() {
		return subTexturesPerSide;
	}

}
