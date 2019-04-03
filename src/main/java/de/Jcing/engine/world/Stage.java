package de.jcing.engine.world;

import java.util.HashMap;

import org.joml.Vector2i;

import de.jcing.engine.image.texture.TextureAssembler;

public abstract class Stage {
	
	protected final String name;
	protected final TextureAssembler assembler;
	
	protected final HashMap<Vector2i,Chunk> chunks;
	
	
	public Stage(String name) {
		this.name = name;
		chunks = new HashMap<Vector2i, Chunk>();
		assembler = new TextureAssembler();
		
		
		
	}
	
	
	protected abstract void feedAssembler();
	
	protected abstract void createChunks();
	
}
