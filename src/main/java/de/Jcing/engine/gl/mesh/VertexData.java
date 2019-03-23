package de.jcing.engine.gl.mesh;

public class VertexData {
	
	public float[] positions;
	public float[] texCoords;
	public float[] indices;
	
	public VertexData(float[] positions, float[] texCoords, float[] indices) {
		this.positions = positions;
		this.texCoords = texCoords;
		this.indices = indices;
	}
}
