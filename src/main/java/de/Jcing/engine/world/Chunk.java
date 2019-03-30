package de.jcing.engine.world;

import java.util.LinkedList;
import java.util.Random;

import org.joml.Vector3f;

import de.jcing.engine.gl.mesh.Mesh;
import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.engine.gl.mesh.VertexData;
import de.jcing.engine.texture.Image;
import de.jcing.engine.texture.TextureAtlas;

public class Chunk implements Renderable {

	public final int NUM_TILES = 16;
	
	Vector3f offset;
	
	Tile[][] tiles;
	
	Mesh mesh;
	
	public Chunk(int x, int y, TextureAtlas atlas) {
		offset = new Vector3f(x*NUM_TILES, y*NUM_TILES, 0f);
		tiles = new Tile[NUM_TILES][NUM_TILES];
		Random random = new Random();
		LinkedList<VertexData> datas = new LinkedList<>();
		for(int xi = 0; xi < tiles.length; xi++) {
			for(int yi =0; yi < tiles.length; yi++) {
				tiles[xi][yi] = new Tile(this, xi, yi, new Image(atlas, random.nextInt()%atlas.getSubTextureTotalCount()));
				datas.add(tiles[xi][yi].getVertexData());
			}
		}
		mesh = new Mesh(atlas, datas.toArray(new VertexData[0]));
		
	}
	
	@Override
	public Mesh getMesh() {
		return mesh;
	}
	
	@Override
	public Vector3f getPosition() {
		return offset;
	}
	
}
