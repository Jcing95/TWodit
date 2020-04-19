package de.jcing.engine.opengl.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Renderable {

	protected boolean initialized;
	protected Mesh mesh;

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

	public boolean isInitialized() {
		return initialized;
	}

	public Vector2f getTextureOffset() {
		return mesh.getTexture().getOffset();
	}

	public float getAlpha() {
		return 1f;
	}

}
