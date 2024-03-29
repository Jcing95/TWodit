package de.jcing.engine.opengl.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import de.jcing.engine.image.texture.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mesh {
	
	final Logger LOG = LoggerFactory.getLogger(Mesh.class);
	
	private final int vaoId;

	private final List<Integer> vboIdList;

	private final int vertexCount;

	private final Texture texture;

	public Mesh(Texture texture, VertexData... data) {
		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		IntBuffer indicesBuffer = null;
		this.texture = texture;
		vboIdList = new ArrayList<>();
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		int posSize = 0;
		int texSize = 0;
		int indiceSize = 0;
		for (VertexData d : data) {
			posSize += d.positions.length;
			texSize += d.texCoords.length;
			indiceSize += d.indices.length;
		}
		vertexCount = posSize;
		LOG.debug("datacount: " + data.length);
		LOG.debug("posSize: " + posSize);
		LOG.debug("texSize: " + texSize);
		LOG.debug("indiceSize: " + indiceSize);

		try {
			posBuffer = MemoryUtil.memAllocFloat(posSize);
			textCoordsBuffer = MemoryUtil.memAllocFloat(texSize);
			indicesBuffer = MemoryUtil.memAllocInt(indiceSize);
			int vertexOffset = 0;

			for (VertexData d : data) {
				posBuffer.put(d.positions, 0, d.positions.length);
				textCoordsBuffer.put(d.texCoords, 0, d.texCoords.length);

				for (int i = 0; i < d.indices.length; i++) {
					d.indices[i] += vertexOffset / 3;
				}

				indicesBuffer.put(d.indices, 0, d.indices.length);
				vertexOffset += d.positions.length;

			}

			posBuffer.flip();
			textCoordsBuffer.flip();
			indicesBuffer.flip();

			// Position VBO
			int vboId = glGenBuffers();
			vboIdList.add(vboId);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			// Texture coordinates VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			// Index VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		} finally {
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public Mesh(Texture texture, VertexData data) {
		this(data.positions, data.texCoords, data.indices, texture);
	}

	public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {

		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		IntBuffer indicesBuffer = null;

		try {
			this.texture = texture;
			vertexCount = indices.length;
			vboIdList = new ArrayList<>();

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Position VBO
			int vboId = glGenBuffers();
			vboIdList.add(vboId);
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Texture coordinates VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			textCoordsBuffer.put(textCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			// Index VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		} finally {
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Texture getTexture() {
		return texture;
	}

	public void render() {
		// Activate firs texture bank
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, texture.getId());

		// Draw the mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : vboIdList) {
			glDeleteBuffers(vboId);
		}

		// Delete the texture
		texture.cleanup();

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

}
