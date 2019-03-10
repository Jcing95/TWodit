package de.jcing.engine.gl;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private final int vaoId;
	private final int vboId;
	private final int vertexCount;

	public Mesh(float[] positions) {
		FloatBuffer verticesBuffer = null;
		try {
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			vertexCount = positions.length / 3;
			verticesBuffer.put(positions).flip();
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			vboId = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verticesBuffer, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
		}
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanUp() {
		GL30.glDisableVertexAttribArray(0);
		// Delete the VBO
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glDeleteBuffers(vboId);
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}

}
