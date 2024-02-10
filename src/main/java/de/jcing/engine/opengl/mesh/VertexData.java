package de.jcing.engine.opengl.mesh;

import java.util.ArrayList;

public class VertexData {

	public final float[] positions;
	public final float[] texCoords;
	public final int[] indices;

	public VertexData(float[] positions, float[] texCoords, int[] indices) {
		this.positions = positions;
		this.texCoords = texCoords;
		this.indices = indices;
	}

	public VertexData(VertexData... toCombine) {
		ArrayList<Float> pos = new ArrayList<>();
		ArrayList<Float> tex = new ArrayList<>();
		ArrayList<Integer> id = new ArrayList<>();
		int lastPos = 0;
		for (VertexData d : toCombine) {
			for (float f : d.positions)
				pos.add(f);
			for (float f : d.texCoords)
				tex.add(f);
			for (int f : d.indices)
				id.add(f + lastPos);
			lastPos += d.positions.length;
		}
		positions = new float[pos.size()];
		texCoords = new float[tex.size()];
		indices = new int[id.size()];
		for (int i = 0; i < pos.size(); i++) {
			positions[i] = pos.get(i);
		}
		for (int i = 0; i < tex.size(); i++) {
			texCoords[i] = tex.get(i);
		}
		for (int i = 0; i < id.size(); i++) {
			indices[i] = id.get(i);
		}
	}

	public void offset(float x, float y, float z) {
		for (int i = 0; i < positions.length; i += 3) {
			positions[i] += x;
			positions[i + 1] += y;
			positions[i + 2] += z;
		}
	}

	public static VertexData createRectangle(float x, float y, float z, float width, float height, float texX,
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
