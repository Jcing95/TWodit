package de.jcing.engine.opengl;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

@SuppressWarnings("unchecked")
public class UniformBuffer {

	@SuppressWarnings("rawtypes")
	private final HashMap[] maps = { new HashMap<String, Float>(), new HashMap<String, Integer>(), new HashMap<String, Vector3f>(), new HashMap<String, Vector2f>(), new HashMap<String, float[]>(),
			new HashMap<String, Boolean>(), new HashMap<String, Matrix4f>(), };

	public static final int floats = 0;
	public static final int integers = 1;
	public static final int vector3fs = 2;
	public static final int vector2fs = 3;
	public static final int floatArrays = 4;
	public static final int booleans = 5;
	public static final int matrices = 6;

	public void setUniform(String location, float value) {
		maps[floats].put(location, value);
	}

	public void setUniform(String location, int value) {
		maps[integers].put(location, value);
	}

	public void setUniform(String location, Vector3f vector) {
		maps[vector3fs].put(location, vector);
	}

	public void setUniform(String location, Vector2f vector) {
		maps[vector2fs].put(location, vector);
	}

	public void setUniform(String location, float[] value) {
		maps[floatArrays].put(location, value);
	}

	public void setUniform(String location, boolean value) {
		maps[booleans].put(location, value);
	}

	public void setUniform(String location, Matrix4f value) {
		maps[matrices].put(location, value);
	}

	public float getFloat(String location) {
		return (float) maps[floats].get(location);
	}

	public int getInt(String location) {
		return (int) maps[integers].get(location);
	}

	public Vector2f getVector2f(String location) {
		return (Vector2f) maps[vector2fs].get(location);
	}

	public Vector3f getVector3f(String location) {
		return (Vector3f) maps[vector3fs].get(location);
	}

	public float[] getfloatArray(String location) {
		return (float[]) maps[floatArrays].get(location);
	}

	public boolean getBoolean(String location) {
		return (boolean) maps[booleans].get(location);
	}

	public Matrix4f getMatrix(String location) {
		return (Matrix4f) maps[matrices].get(location);
	}

	public void clear() {
		for (HashMap<?, ?> m : maps) {
			m.clear();
		}
	}

}
