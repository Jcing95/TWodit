package de.jcing.engine.entity;

import org.joml.Vector3f;

import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.Sprite;

public class Entity extends Sprite {

	protected Vector3f position;
	protected float scale;
	protected final Vector3f rotation;

	public Entity(Mesh mesh) {
		super(mesh);
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = 1;
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

}
