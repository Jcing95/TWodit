package de.jcing.engine.gl.mesh;

public class VertexData {
	
	public float[] positions;
	public float[] texCoords;
	public int[] indices;
	
	public VertexData(float[] positions, float[] texCoords, int[] indices) {
		this.positions = positions;
		this.texCoords = texCoords;
		this.indices = indices;
	}
	
	public VertexData() {
		
	};
	
	public void setPositions(float[] positions) {
		this.positions = positions;
	}
	
	public void setTexCoords(float[] texCoords) {
		this.texCoords = texCoords;
	}
	
	public void setIndices(int[] indices) {
		this.indices = indices;
	}
}
