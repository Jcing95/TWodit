package de.jcing.engine.texture;

public class SubTexture extends Texture {
	
	private TextureAtlas atlas;
	
	private int index;
	
	public SubTexture(TextureAtlas atlas, int index) {
		super(atlas.getId());
		this.index = index;
		this.atlas = atlas;
	}
	
	public float getX() {
		return index*atlas.getSubTextureSize() % atlas.getWidth();
		
	}
	
	
	
	
}
