package de.jcing.engine.image.texture;

import org.joml.Vector2f;

import de.jcing.utillities.task.Task;

public class Animation extends Image {

	public static int DEFAULT_WAIT_MILLIS = 100;

	private int startIndex;
	private int length;

	private int millisWait;

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
		index = startIndex + ((Task.millis() / millisWait) % length);
	}

	@Override
	public Vector2f getOffset() {
		Image img = new Image(this,startIndex);
		float deltaX = getX() - img.getX();
		float deltaY = getY() - img.getY();
//		if(index != img.getIndex())
//		System.out.println("x" + deltaX + " y" + deltaY );
		return new Vector2f(deltaX,deltaY);
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

	public Image getImage(int index) {
		return new Image(this,startIndex+index);
	}

}
