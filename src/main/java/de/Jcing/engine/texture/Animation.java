package de.jcing.engine.texture;

public class Animation extends TextureAtlas {
	
	public static int DEFAULT_WAIT_MILLIS = 60;
	
	private int startIndex;
	private int length;
	
	private int millisWait;
	
	public Animation(TextureAtlas atlas, int startIndex, int length, int millisWait) {
		super(atlas);
		this.startIndex = startIndex;
		this.length = length;
		this.millisWait = millisWait;
	}
	
	public Animation(TextureAtlas atlas, int startIndex, int length) {
		this(atlas, startIndex, length, DEFAULT_WAIT_MILLIS);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getLength() {
		return length;
	}

	public int getMillisWait() {
		return millisWait;
	}
	
}
