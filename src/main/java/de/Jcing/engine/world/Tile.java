package de.jcing.engine.world;

import org.joml.Vector2i;

import de.jcing.engine.gl.mesh.MeshFactory;
import de.jcing.engine.gl.mesh.VertexData;
import de.jcing.engine.image.texture.Image;

public class Tile {

	private Image texture;

	private final Vector2i pos;

	private Chunk chunk;

	private final VertexData data;

	public Tile(Chunk chunk, int xPos, int yPos, Image tex) {
		this.chunk = chunk;
		this.pos = new Vector2i(xPos, yPos);
		this.texture = tex;
		data = MeshFactory.createRectData(xPos, yPos, 0, 1, 1, tex);
	}

	public VertexData getVertexData() {
		return data;
	}

}
