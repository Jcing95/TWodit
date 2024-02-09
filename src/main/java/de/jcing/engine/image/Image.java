package de.jcing.engine.image;

import org.joml.Vector2f;

public class Image {

    protected int index;

    private final TextureAtlas atlas;

	public Image(TextureAtlas atlas, int index) {
        this.atlas = atlas;
		this.index = index % atlas.getSubTextureTotalCount();
	}

	public float getX() {
		return (index % atlas.getNumTexturesHorizontal()) * atlas.getTextureWidth();
	}

	public float getY() {
		return (index / atlas.getNumTexturesHorizontal()) * atlas.getTextureHeight();
	}

    public float getWidth() {
        return atlas.getTextureWidth();
    }

    public float getHeight() {
        return atlas.getTextureHeight();
    }

	public int getIndex() {
		return index;
	}

    public void setIndex(int index) {
        this.index = index % atlas.getSubTextureTotalCount();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Vector2f getOffset() {
        return new Vector2f(getX(), getY());
    }
}
