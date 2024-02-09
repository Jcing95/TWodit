package de.jcing.engine.opengl.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Sprite {

	protected Mesh mesh;
	
	public Sprite(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Vector3f getPosition() {
		return new Vector3f(0, 0, 0);
	}

	public float getScale() {
		return 1.0f;
	}

	public Vector3f getRotation() {
		return new Vector3f(0, 0, 0);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public float getAlpha() {
		return 1f;
	}

	public Vector2f getTextureOffset() {
		return new Vector2f(0,0);
	}

}
