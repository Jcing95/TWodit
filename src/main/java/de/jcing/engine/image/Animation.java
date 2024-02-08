package de.jcing.engine.image;

import de.jcing.util.Clock;
import org.joml.Vector2f;

public class Animation extends Image {

	public static final int DEFAULT_WAIT_MILLIS = 100;

	private int startIndex;
	private final int length;

	private final int millisWait;

	public Animation(TextureAtlas atlas, int startIndex, int length, int millisWait) {
		super(atlas, startIndex);
		this.startIndex = startIndex;
		this.length = length;
		this.millisWait = millisWait;
	}

	public Animation(TextureAtlas atlas, int startIndex, int length) {
		this(atlas, startIndex, length, DEFAULT_WAIT_MILLIS);
	}

	public void update() {
		index = startIndex + ((Clock.millis() / millisWait) % length);
	}

	public Vector2f getOffset() {
		return new Vector2f(getX(), getY());
	}

	public int getLength() {
		return length;
	}

	public void set(Animation anim) {
		super.set(anim);
		startIndex = anim.startIndex;
	}

	public Image getImage(int index) {
		return new Image(this, startIndex + index);
	}

	public void reset() {
		index = startIndex;
	}

}
