package de.jcing.game.stages;

import java.util.HashMap;

import org.joml.Vector2i;

import de.jcing.engine.image.Image;
import de.jcing.engine.image.TextureAtlas;
import de.jcing.engine.opengl.Camera;
import de.jcing.engine.opengl.Renderer;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.Sprite;
import de.jcing.engine.opengl.mesh.VertexData;
import de.jcing.engine.world.Chunk;
import de.jcing.game.Player;

public class Stage {

	private final HashMap<Vector2i, Chunk> chunks;

	private final Renderer renderer;

	private final Camera camera;

	private final Player player;

	private Sprite piece;
	private TextureAtlas tilemap;

	public Stage(Renderer renderer, TextureAtlas tilemap, Player player) {
		this.renderer = renderer;
		this.camera = renderer.getCamera();
		this.tilemap = tilemap;
		this.player = player;

		chunks = new HashMap<>();
		createChunks(chunks);
		prepareRenderer(renderer);

	}

	protected void createChunks(HashMap<Vector2i, Chunk> chunks) {
		for (int x = -10; x < 10; x++)
			for (int y = -10; y < 10; y++)
				chunks.put(new Vector2i(x, y), new Chunk(x, y).init(tilemap));
		piece = createRenderableStage();
	}

	public Sprite createRenderableStage() {
		VertexData[] datas = new VertexData[chunks.size()];
		int i = 0;
		for (Chunk c : chunks.values()) {
			datas[i] = c.getVertices();
			i++;
		}
		Mesh mesh = new Mesh(tilemap, datas);
		return new Sprite(mesh);
	}

	public void prepareRenderer(Renderer r) {
		// for (Chunk c : chunks.values())
		r.addRenderable(r.getTerrainShader(), piece);
		r.addRenderable(r.getEntityShader(), player);
	}

	public void tick() {
		player.tick(camera);
		renderer.bufferWorldMatrix(player);
		renderer.bufferWorldMatrix(piece);
		renderer.swapMatrixBuffer();
	}

}
