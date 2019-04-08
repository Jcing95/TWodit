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
	
	public Stage(String name, Renderer r, Camera cam) {
		this.name = name;
		this.renderer = r;
		this.camera = cam;
		chunks = new HashMap<Vector2i, Chunk>();
		assembler = new TextureAssembler();
	}
	
	protected void init() {
		feedAssembler(assembler);
		assembler.buildAtlas();
		createChunks(chunks, assembler);
		prepareRenderer(renderer);
	}
	
	
	protected abstract void feedAssembler(TextureAssembler assembler);
	
	protected abstract void createChunks(HashMap<Vector2i, Chunk> chunks, TextureAssembler assembler);
	
	public abstract void tick();
	
	public abstract void prepareRenderer(Renderer r);
	
}
