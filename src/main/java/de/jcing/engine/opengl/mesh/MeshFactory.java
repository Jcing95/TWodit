package de.jcing.engine.opengl.mesh;

public class MeshFactory {

	public static VertexData createRectData(float x, float y, float z, float width, float height, float texX,
			float texY, float texWidth, float texHeight) {
		float[] vertices = new float[12];

		// links oben
		vertices[0] = x;
		vertices[1] = y;
		vertices[2] = z;
		// rechts oben
		vertices[3] = x + width;
		vertices[4] = y;
		vertices[5] = z;
		// links unten
		vertices[6] = x;
		vertices[7] = y + height;
		vertices[8] = z;
		// rechts unten
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

		float tw = texWidth;
		float th = texHeight;
		float tx = texX;
		float ty = texY;

		float[] textureCoordinates = new float[8];
		textureCoordinates[0] = tx;
		textureCoordinates[1] = ty;

		textureCoordinates[2] = tx + tw;
		textureCoordinates[3] = ty;

		textureCoordinates[4] = tx;
		textureCoordinates[5] = ty + th;

		textureCoordinates[6] = tx + tw;
		textureCoordinates[7] = ty + th;

		return new VertexData(vertices, textureCoordinates, indices);
	}

}
