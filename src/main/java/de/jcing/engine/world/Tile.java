package de.jcing.engine.world;

import de.jcing.engine.image.Image;
import de.jcing.engine.opengl.mesh.VertexData;

public class Tile {

	private final VertexData data;

	public Tile(Chunk chunk, int xPos, int yPos, Image tex) {
		data = VertexData.createRectangle(xPos, yPos, 0, 1, 1, tex.getX(), tex.getY(), tex.getWidth(), tex.getHeight());
	}

	public VertexData getVertexData() {
		return data;
	}

}
