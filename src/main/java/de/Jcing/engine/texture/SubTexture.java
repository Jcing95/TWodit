package de.jcing.engine.texture;

public class SubTexture extends Texture {
	
	private TextureAtlas atlas;
	
	private int index;
	
	public SubTexture(TextureAtlas atlas, int index) {
		super(atlas);
		this.index = index;
		this.atlas = atlas;
	}
	
	public float getX() {
		return (float)(index % atlas.getSubTexturesPerSide()) / atlas.getSubTexturesPerSide();
	}
	
	public float getY() {
		return (float)(index / atlas.getSubTexturesPerSide()) / atlas.getSubTexturesPerSide();
	}
	
	public float getWidth() {
		return 1.0f/atlas.getSubTexturesPerSide();
	}
		
}
