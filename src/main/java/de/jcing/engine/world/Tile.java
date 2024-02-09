package de.jcing.engine.world;

import org.joml.Vector2i;

import de.jcing.engine.image.Image;
import de.jcing.engine.opengl.mesh.MeshFactory;
import de.jcing.engine.opengl.mesh.VertexData;

public class Tile {

	private final VertexData data;

	public Tile(Chunk chunk, int xPos, int yPos, Image tex) {
		data = MeshFactory.createRectData(xPos, yPos, 0, 1, 1, tex.getX(), tex.getY(), tex.getWidth(), tex.getHeight());
	}

	public VertexData getVertexData() {
		return data;
	}

}
