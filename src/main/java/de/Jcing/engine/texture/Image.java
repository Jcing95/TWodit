package de.jcing.engine.texture;

public class Image extends TextureAtlas {
		
	private int index;
	
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
