package de.jcing.engine.opengl.mesh;

import de.jcing.engine.image.texture.Image;

public class MeshFactory {

	public static VertexData createRectData(float x, float y, float z, float width, float height, Image texture) {
		return createRectData(x, y, z, width, height, texture, true);
	}

	public static VertexData createRectData(float x, float y, float z, float width, float height, Image texture, boolean texture_offset) {
		VertexData data = new VertexData();
		float[] vertices = new float[12];

		//links oben
		vertices[0] = x;
		vertices[1] = y;
		vertices[2] = z;
		//rechts oben
		vertices[3] = x + width;
		vertices[4] = y;
		vertices[5] = z;
		//links unten
		vertices[6] = x;
		vertices[7] = y + height;
		vertices[8] = z;
		//rechts unten
		vertices[9] = x + width;
		vertices[10] = y + height;
		vertices[11] = z;

		int[] indices = new int[6];
		indices[0] = 0;
		indices[1] = 1;
		indices[2] = 3;

		indices[3] = 0;
		indices[4] = 2;
		indices[5] = 3;

		float ts = texture.getWidth();
		float tx = texture_offset ? texture.getX() : 0;
		float ty = texture_offset ? texture.getY() : 0;

		float[] textureCoordinates = new float[8];
		textureCoordinates[0] = tx;
		textureCoordinates[1] = ty;

		textureCoordinates[2] = tx + ts;
		textureCoordinates[3] = ty;

		textureCoordinates[4] = tx;
		textureCoordinates[5] = ty + ts;

		textureCoordinates[6] = tx + ts;
		textureCoordinates[7] = ty + ts;

		data.setPositions(vertices);
		data.setIndices(indices);
		data.setTexCoords(textureCoordinates);

		return data;
	}

}
