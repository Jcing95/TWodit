package de.jcing.engine.world;

import java.util.Random;

import org.joml.Vector2i;
import org.joml.Vector3f;

import de.jcing.engine.image.Animation;
import de.jcing.engine.opengl.mesh.VertexData;

public class Chunk {

	public final int NUM_TILES = 16;

	final Vector3f offset;
	final Vector2i position;

	final Tile[][] tiles;
	VertexData data;

	public Chunk(int x, int y) {
		position = new Vector2i(x, y);
		offset = new Vector3f(x * NUM_TILES, y * NUM_TILES, 0f);
		tiles = new Tile[NUM_TILES][NUM_TILES];
	}

	public Chunk init(Animation images) {
		Random random = new Random();
		VertexData[] datas = new VertexData[NUM_TILES * NUM_TILES];
		for (int xi = 0; xi < tiles.length; xi++) {
			for (int yi = 0; yi < tiles.length; yi++) {
				tiles[xi][yi] = new Tile(this, xi, yi, images.getImage(random.nextInt(images.getLength())));
				datas[xi*tiles.length+yi] = tiles[xi][yi].getVertexData();
			}
		}
		data = new VertexData(datas);
		data.offset(offset.x, offset.y, 0);
		return this;
	}
	
	public VertexData getVertices() {
		return data;
	}

}
