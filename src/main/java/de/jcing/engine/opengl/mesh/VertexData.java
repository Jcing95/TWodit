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
	
	public VertexData(VertexData...toCombine) {
		ArrayList<Float> pos = new ArrayList<>();
		ArrayList<Float> tex = new ArrayList<>();
		ArrayList<Integer> id = new ArrayList<>();
		int lastPos = 0;
		for(VertexData d : toCombine) {
			for(float f: d.positions)
				pos.add(f);
			for(float f: d.texCoords)
				tex.add(f);
			for(int f: d.indices)
				id.add(f+lastPos);
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
		for (int i = 0; i < positions.length; i+=3) {
			positions[i] += x;
			positions[i+1] += y;
			positions[i+2] += z;
		}
	}
	
}
