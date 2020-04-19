package de.jcing.engine.entity;

import org.joml.Vector3f;

import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.Renderable;

public class Entity extends Renderable {

	protected Vector3f position;
	protected float scale;
	protected Vector3f rotation;

	public Entity() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = 1;
		initialized = false;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
		initialized = true;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	public void bindPosition(Vector3f pos) {
		this.position = pos;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void movePosition(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Mesh getMesh() {
		return mesh;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
