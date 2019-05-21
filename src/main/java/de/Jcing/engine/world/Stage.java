package de.jcing.engine.world;

import java.util.HashMap;

import org.joml.Vector2i;

import de.jcing.engine.gl.Camera;
import de.jcing.engine.gl.Renderer;
import de.jcing.engine.image.texture.TextureAssembler;

public abstract class Stage {
	
	protected final String name;
	protected final TextureAssembler assembler;
	
	protected final HashMap<Vector2i,Chunk> chunks;
	
	protected final Renderer renderer;
	
	protected Camera camera;
	
	public Stage(String name, Renderer renderer, Camera camera) {
		this.name = name;
		this.renderer = renderer;
		this.camera = camera;
		
		//store chunks in a IntegerVector - Chunk hashmap so that stages do not have to be rectangular.
		chunks = new HashMap<Vector2i, Chunk>();
		assembler = new TextureAssembler();
	}
	
	protected void init() {
		feedAssembler(assembler);
		assembler.buildAtlas();
		createChunks(chunks, assembler);
		prepareRenderer(renderer);
	}
	
	/**
	 * override this method to load all Images to the TextureAssembler whom will create an Atlas.
	 * 
	 * @param assembler - feed images here
	 */
	protected abstract void feedAssembler(TextureAssembler assembler);
	
	
	/**
	 * Initialize all chunks in this method.
	 * 
	 * @param chunks store chunks in here.
	 * @param assembler use this assembler to provide textures to the chunks
	 */
	protected abstract void createChunks(HashMap<Vector2i, Chunk> chunks, TextureAssembler assembler);
	
	
	/**
	 * handle stage logic in here.
	 * Also buffer Position Matrices here and swap buffers!
	 * 
	 */
	public abstract void tick();
	
	/**
	 * Add all renderables to the Renderer in here
	 * 
	 */
	public abstract void prepareRenderer(Renderer r);
	
}
