package de.jcing.engine.gl.mesh;

import org.joml.Vector3f;

public interface Renderable {


	default public Vector3f getPosition() {
		return new Vector3f(0,0,0);
	}

	default public float getScale() {
		return 1.0f;
	}

	default public Vector3f getRotation() {
		return new Vector3f(0,0,0);
	}

	public Mesh getMesh();
}
