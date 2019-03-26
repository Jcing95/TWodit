package de.jcing.engine.world;

import org.joml.Vector3f;

import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.engine.gl.mesh.VertexData;
import de.jcing.engine.texture.Image;

public class Tile implements Renderable {
	
	private Image texture;
	
	private int xPos, yPos;
	
	private Chunk chunk;
	
	private VertexData data;
	
	private Mesh mesh;
	
	public Tile(Chunk chunk, int xPos, int yPos, Image tex) {
		this.chunk = chunk;
		this.xPos = xPos;
		this.yPos = yPos;
		this.texture = tex;
		data = new VertexData();
		float[] vertices = new float[12];
		
		//links oben
		vertices[0] = xPos;
		vertices[1] = yPos;
		vertices[2] = 0;
		//rechts oben
		vertices[3] = xPos+1;
		vertices[4] = yPos;
		vertices[5] = 0;
		//links unten
		vertices[6] = xPos;
		vertices[7] = yPos+1;
		vertices[8] = 0;
		//rechts unten
		vertices[9] = xPos+1;
		vertices[10] = yPos+1;
		vertices[11] = 0;
		
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
		mesh = new Mesh(texture, data);
	}
	
	@Override
	public Vector3f getPosition() {
		return new Vector3f(chunk.getOffset().x, chunk.getOffset().y, chunk.getOffset().z);
	}
	
	public VertexData getVertexData() {
		return data;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}
