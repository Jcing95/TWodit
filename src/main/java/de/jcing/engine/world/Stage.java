package de.jcing.engine.world;

import java.util.HashMap;

import org.joml.Vector2i;

import de.jcing.engine.opengl.Camera;
import de.jcing.engine.opengl.Renderer;

public abstract class Stage {

	protected final String name;

	protected final HashMap<Vector2i, Chunk> chunks;

	protected final Renderer renderer;

	protected final Camera camera;

	public Stage(String name, Renderer renderer, Camera camera) {
		this.name = name;
		this.renderer = renderer;
		this.camera = camera;

		//store chunks in a IntegerVector - Chunk hashmap so that stages do not have to be rectangular.
		chunks = new HashMap<>();
	}

	protected void init() {
		createChunks(chunks);
		prepareRenderer(renderer);
	}

	/**
	 * Initialize all chunks in this method.
	 * 
	 * @param chunks    store chunks in here.
	 */
	protected abstract void createChunks(HashMap<Vector2i, Chunk> chunks);

	/**
	 * handle stage logic in here. Also buffer Position Matrices here and swap
	 * buffers!
	 * 
	 */
	public abstract void tick();

	/**
	 * Add all renderables to the Renderer in here
	 * 
	 */
	public abstract void prepareRenderer(Renderer r);

}
