package de.jcing.engine.world;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2i;
import org.joml.Vector3f;

import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.engine.gl.mesh.VertexData;
import de.jcing.engine.image.texture.Animation;
import de.jcing.engine.image.texture.TextureAssembler;

public class Chunk extends Renderable {

	public final int NUM_TILES = 16;
	
	Vector3f offset;
	Vector2i position;
	
	Tile[][] tiles;
	
	public Chunk(int x, int y, TextureAssembler as) {
		initialized = false;
		position = new Vector2i(x,y);
		offset = new Vector3f(x*NUM_TILES, y*NUM_TILES, 0f);
		tiles = new Tile[NUM_TILES][NUM_TILES];
	}
	
	public Chunk init(Animation images) {
		Random random = new Random();
		ArrayList<VertexData> datas = new ArrayList<>(NUM_TILES*NUM_TILES);
		for(int xi = 0; xi < tiles.length; xi++) {
			for(int yi =0; yi < tiles.length; yi++) {
				tiles[xi][yi] = new Tile(this, xi, yi, images.getImage(random.nextInt(images.getLength())));
				datas.add(tiles[xi][yi].getVertexData());
			}
		}
		mesh = new Mesh(images, datas.toArray(new VertexData[0]));		
		initialized = true;
		return this;
	}
	
	
	@Override
	public Vector3f getPosition() {
		return offset;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
}
