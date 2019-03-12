package de.jcing.engine.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private final int vaoId;
	private final int posVboId;
	private final int idxVboId;
	private final int colourVboId;
	private final int vertexCount;

	public Mesh(float[] positions, float[] colours, int[] indices) {
		FloatBuffer verticesBuffer = null;
		FloatBuffer colourBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			vertexCount = indices.length;
			verticesBuffer.put(positions).flip();
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			
			posVboId = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, posVboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verticesBuffer, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
			
			// Colour VBO
            colourVboId = GL30.glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, colourVboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, colourBuffer, GL30.GL_STATIC_DRAW);
            GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 0, 0);
			
			// Index VBO
            idxVboId = GL30.glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);
            
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			
			

		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
				MemoryUtil.memFree(colourBuffer);
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}
	
	public void render() {
		GL30.glBindVertexArray(getVaoId());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glDrawElements(GL30.GL_TRIANGLES, getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
		// Restore state
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
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
		GL30.glDeleteBuffers(posVboId);
		GL30.glDeleteBuffers(idxVboId);
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}

}
