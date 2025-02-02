package de.jcing.game.stages;

import java.util.HashMap;

import org.joml.Vector2i;

import de.jcing.engine.image.JMultiImage;
import de.jcing.engine.image.texture.TextureAssembler;
import de.jcing.engine.opengl.Renderer;
import de.jcing.engine.opengl.Transformation;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.Renderable;
import de.jcing.engine.opengl.mesh.VertexData;
import de.jcing.engine.world.Chunk;
import de.jcing.engine.world.Stage;
import de.jcing.game.Player;

public class MainStage extends Stage {

	final Player player;

	Transformation transformation;

	private int grass;
	private Renderable piece;
	
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
				chunks.put(new Vector2i(x, y), new Chunk(x, y).init(assembler.getAnimation(grass)));
		piece = createRenderableStage();
	}
	
	public Renderable createRenderableStage() {
		VertexData[] datas = new VertexData[chunks.size()];
		int i = 0;
		for(Chunk c : chunks.values()) {
			datas[i] = c.getVertices();
			i++;
		}
		Mesh mesh = new Mesh(assembler.getAnimation(grass), datas);
		return new Renderable(mesh);
	}
	
	
	@Override
	public void prepareRenderer(Renderer r) {
//		for (Chunk c : chunks.values())
			r.addRenderable(r.getTerrainShader(), piece);
		r.addRenderable(r.getEntityShader(), player);
	}

	@Override
	public void tick() {
		player.tick(camera);
		renderer.bufferWorldMatrix(player);
		renderer.bufferWorldMatrix(piece);
		renderer.swapMatrixBuffer();
	}

}
