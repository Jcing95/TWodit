package de.jcing.engine.opengl.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import de.jcing.utillities.log.Log;

public abstract class Shader {

	public static final String SHADER_LOCATION = "";
	protected int programID;
	protected int vertexShaderID;
	protected int fragmentShaderID;

	//	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private final HashMap<String, Integer> uniforms;

	public Shader(String vertexFile, String fragmentFile) {
		programID = GL30.glCreateProgram();
		vertexShaderID = loadShader(vertexFile, GL30.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL30.GL_FRAGMENT_SHADER);
		uniforms = new HashMap<>();
	}

	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			Log.error("Could not read " + file);
			e.printStackTrace();
			//TODO: exit here?
			System.exit(-1);
		}
		int shaderID = GL30.glCreateShader(type);
		GL30.glShaderSource(shaderID, shaderSource);
		GL30.glCompileShader(shaderID);

		if (GL30.glGetShaderi(shaderID, GL30.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			Log.info(GL30.glGetShaderInfoLog(shaderID, 500));
			Log.error("Could not compile shader.");
			System.exit(-1);
		}
		GL30.glAttachShader(programID, shaderID);
		return shaderID;
	}

	public void link() throws Exception {
		GL30.glLinkProgram(programID);
		if (GL30.glGetProgrami(programID, GL30.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new Exception("Error linking Shader code: " + GL30.glGetProgramInfoLog(programID, 1024));
		}
		if (vertexShaderID != 0) {
			GL30.glDetachShader(programID, vertexShaderID);
		}
		if (fragmentShaderID != 0) {
			GL30.glDetachShader(programID, fragmentShaderID);
		}
		GL30.glValidateProgram(programID);
		if (GL30.glGetProgrami(programID, GL30.GL_VALIDATE_STATUS) == 0) {
			Log.error("Warning validating Shader code: " + GL30.glGetProgramInfoLog(programID, 1024));
		}
	}

	public void unbind() {
		GL30.glUseProgram(0);
	}

	public void bind() {
		GL30.glUseProgram(programID);
	}

	public void cleanUp() {
		unbind();
		if (programID != 0)
			GL30.glDeleteProgram(programID);
	}

	public void createUniform(String uniformName) {
		int uniformLocation = GL30.glGetUniformLocation(programID, uniformName);
		if (uniformLocation < 0) {
			Log.error("Could not find uniform:" + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String location, float value) {
		GL30.glUniform1f(uniforms.get(location), value);
	}

	public void setUniform(String location, int value) {
		GL30.glUniform1i(uniforms.get(location), value);
	}

	public void setUniform(String location, Vector3f vector) {
		GL30.glUniform3f(uniforms.get(location), vector.x, vector.y, vector.z);
	}

	public void setUniform(String location, Vector2f vector) {
		GL30.glUniform2f(uniforms.get(location), vector.x, vector.y);
	}

	public void setUniform(String location, float[] value) {
		GL30.glUniform1fv(uniforms.get(location), value);
	}

	public void setUniform(String location, boolean value) {
		GL30.glUniform1f(uniforms.get(location), value ? 1f : 0f);
	}

	public void setUniform(String location, Matrix4f value) {
		// Dump the matrix into a float buffer
		if(value!=null)
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			GL30.glUniformMatrix4fv(uniforms.get(location), false, fb);
		}
	}

	public void bindAttribute(int attribute, String variableName) {
		GL30.glBindAttribLocation(programID, attribute, variableName);
	}
}