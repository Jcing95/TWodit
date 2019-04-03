package de.jcing.engine.image.texture;

public class Image extends TextureAtlas {
		
	protected int index;
	
	public Image(TextureAtlas atlas, int index) {
		super(atlas);
		this.index = index % atlas.getSubTextureTotalCount();
	}
	
	public float getX() {
		return (float)(index % getSubTexturesPerSide()) / getSubTexturesPerSide();
	}
	
	public float getY() {
		return (float)(index / getSubTexturesPerSide()) / getSubTexturesPerSide();
	}
	
	public float getWidth() {
		return 1.0f/getSubTexturesPerSide();
	}
		
}
