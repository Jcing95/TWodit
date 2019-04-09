package de.jcing.game.stages;

import java.util.HashMap;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import de.jcing.engine.gl.Renderer;
import de.jcing.engine.gl.Transformation;
import de.jcing.engine.image.JMultiImage;
import de.jcing.engine.image.texture.AtlasCallback;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.world.Chunk;
import de.jcing.engine.world.Stage;
import de.jcing.game.Player;

public class MainStage extends Stage {

	
	Player player;
	
	Transformation transformation;
	
	private int grass;
	
	public MainStage(Renderer renderer) {
		super("Main", renderer, renderer.getCamera());
		player = new Player(assembler);
		init();
	}


	@Override
	protected void feedAssembler(TextureAssembler assembler) {
		JMultiImage grass = new JMultiImage("gfx/terrain/grass");
		this.grass = assembler.addFrames(grass);
	}

	@Override
	protected void createChunks(HashMap<Vector2i, Chunk> chunks, TextureAssembler assembler) {
		for (int x = -10; x < 10; x++)
			for (int y = -10; y < 10; y++)
				chunks.put(new Vector2i(x,y), new Chunk(x, y, assembler).init(assembler.getAnimation(grass)));
	}

	public void prepareRenderer(Renderer r) {
		for(Chunk c : chunks.values())
			r.addRenderable(r.getTerrainShader(),c);
		r.addRenderable(r.getEntityShader(),player);
	}
	
	@Override
	public void tick() {
		player.tick(camera);
		renderer.bufferWorldMatrix(player);
		for(Chunk c : chunks.values())
			renderer.bufferWorldMatrix(c);
		renderer.swapUniformBuffer();
	}

}
