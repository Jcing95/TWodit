package de.jcing.engine.world;

import org.joml.Vector3f;

public class Chunk {

	public final int NUM_TILES = 16;
	
	Vector3f offset;
	
	
	
	public Chunk(int x, int y) {
		offset = new Vector3f(x*NUM_TILES, y*NUM_TILES, 0f);
	}
	
	public Vector3f getOffset() {
		return offset;
	}
	
}
