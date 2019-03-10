package de.jcing.engine.gl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.jcing.utillities.log.Log;

public abstract class Shader {
	
	public static final String SHADER_LOCATION = "";
	public static final Log log = new Log(Shader.class);
	
	protected int programID;
	protected int vertexShaderID;
	protected int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	
	public Shader(String vertexFile, String fragmentFile) {
		programID = GL30.glCreateProgram();
		vertexShaderID = loadShader(vertexFile, GL30.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL30.GL_FRAGMENT_SHADER);
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
		}
		catch (IOException e) {
			log.error("Could not read " + file);
			e.printStackTrace();
			//TODO: exit here?
			System.exit(-1);
		}
		int shaderID = GL30.glCreateShader(type);
		GL30.glShaderSource(shaderID, shaderSource);
		GL30.glCompileShader(shaderID);
		
		if (GL30.glGetShaderi(shaderID, GL30.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			log.info(GL30.glGetShaderInfoLog(shaderID, 500));
			log.error("Could not compile shader.");
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
			log.error("Warning validating Shader code: " + GL30.glGetProgramInfoLog(programID, 1024));
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
		if(programID != 0)
			GL30.glDeleteProgram(programID);
	}

	protected void loadFloat(int location, float value) {
		GL30.glUniform1f(location, value);
	}

	protected void loadInt(int location, int value) {
		GL30.glUniform1i(location, value);
	}

	protected void loadVector(int location, Vector3f vector) {
		GL30.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	protected void load2DVector(int location, Vector2f vector) {
		GL30.glUniform2f(location, vector.x, vector.y);
	}

	protected void loadFloatArray(int location, float[] value) {
		GL30.glUniform1fv(location, value);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL30.glUniform1f(location, value ? 1f : 0f);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(matrixBuffer);
		matrixBuffer.flip();
		GL30.glUniformMatrix4fv(location, false, matrixBuffer);
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL30.glBindAttribLocation(programID, attribute, variableName);
	}
}