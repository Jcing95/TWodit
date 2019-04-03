package de.jcing.game.stages;

import java.util.HashMap;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import de.jcing.engine.gl.Renderer;
import de.jcing.engine.gl.Transformation;
import de.jcing.engine.image.JMultiImage;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.world.Chunk;
import de.jcing.engine.world.Stage;
import de.jcing.game.Player;

public class MainStage extends Stage {

	
	Player player;
	
	Transformation transformation;
	
	private static final float DEFAULT_SPEED = 0.2f;
	private static final float SHIFT_MULT = 2.5f;
	
	public MainStage(Renderer renderer) {
		super("Main", renderer, renderer.getCamera());

	}

	@Override
	protected void feedAssembler(TextureAssembler assembler) {
		JMultiImage grass = new JMultiImage("gfx/terrain/grass");
		assembler.addFrames(grass, null);
	}

	@Override
	protected void createChunks(HashMap<Vector2i, Chunk> chunks, TextureAssembler assembler) {
		for (int x = -10; x < 10; x++)
			for (int y = -10; y < 10; y++)
				chunks.put(new Vector2i(x,y), new Chunk(x, y, assembler));
	}

	public void prepareRenderer(Renderer r) {
		for(Chunk c : chunks.values())
			r.addRenderable(c);
	}
	
	@Override
	public void tick() {
		float speed = DEFAULT_SPEED;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			speed *= SHIFT_MULT;
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_W))
			camera.movePosition(0, speed, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_A))
			camera.movePosition(-speed, 0, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_S))
			camera.movePosition(0, -speed, 0);
		if (KeyBoard.isPressed(GLFW.GLFW_KEY_D))
			camera.movePosition(speed, 0, 0);
	}

}
