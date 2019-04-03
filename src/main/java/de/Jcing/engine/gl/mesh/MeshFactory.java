package de.jcing.engine.gl.mesh;

import de.jcing.engine.image.texture.Image;

public class MeshFactory {
	
	public static VertexData createRectData(float x, float y, float z, float width, float height, Image texture) {
		VertexData data = new VertexData();
		float[] vertices = new float[12];
		
		//links oben
		vertices[0] = x;
		vertices[1] = y;
		vertices[2] = z;
		//rechts oben
		vertices[3] = x+width;
		vertices[4] = y;
		vertices[5] = z;
		//links unten
		vertices[6] = x;
		vertices[7] = y+height;
		vertices[8] = z;
		//rechts unten
		vertices[9] = x+width;
		vertices[10] = y+height;
		vertices[11] = z;
		
		int[] indices = new int[6];
		indices[0] = 0;
		indices[1] = 1;
		indices[2] = 3;
		
		indices[3] = 0;
		indices[4] = 2;
		indices[5] = 3;
		
		float[] textureCoordinates = new float[8];
		textureCoordinates[0] = texture.getX();
		textureCoordinates[1] = texture.getY();
		
		textureCoordinates[2] = texture.getX()+texture.getWidth();
		textureCoordinates[3] = texture.getY();
		
		textureCoordinates[4] = texture.getX();
		textureCoordinates[5] = texture.getY()+texture.getWidth();
		
		textureCoordinates[6] = texture.getX()+texture.getWidth();
		textureCoordinates[7] = texture.getY()+texture.getWidth();
		
		data.setPositions(vertices);
		data.setIndices(indices);
		data.setTexCoords(textureCoordinates);
		
		return data;
	}
	
	
}
