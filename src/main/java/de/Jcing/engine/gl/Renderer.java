package de.jcing.engine.gl;

import java.util.HashMap;
import java.util.LinkedList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.engine.gl.shaders.EntityShader;
import de.jcing.engine.gl.shaders.Shader;
import de.jcing.engine.gl.shaders.TerrainShader;
import de.jcing.engine.window.Window;
import de.jcing.utillities.log.Log;

public class Renderer {

	private static final Log log = new Log(Renderer.class);

	/**
	 * Field of View in Radians
	 */
	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private Window window;

	private TerrainShader terrainShader;
	private EntityShader entityShader;

	private Transformation transformation;

	private Camera camera;

	private HashMap<Shader, LinkedList<Renderable>> items;

	public Renderer(Window win) {
		this.window = win;
		transformation = new Transformation();
		camera = new Camera();
		items = new HashMap<>();

		camera.setPosition(0, 0, 5);
		camera.setRotation(-45f, 0, 0);

		window.runInContext(() -> init());
	}

	private void init() {
		try {
			log.debug("init shader");
			terrainShader = new TerrainShader();
			entityShader = new EntityShader();
			items.put(terrainShader, new LinkedList<>());
			items.put(entityShader, new LinkedList<>());

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());

		} catch (Exception e) {
			e.printStackTrace();
		}

		window.loopInContext(() -> render());

	}

	public void render() {
		if (window.isResized()) {
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		// update transformation
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		drawTerrain(projectionMatrix);
		drawEntities(projectionMatrix);

		//TODO: DRAW GUI HERE
	}
	
	private void drawTerrain(Matrix4f projectionMatrix) {
		terrainShader.bind();
		terrainShader.setUniform(TerrainShader.PROJECTION_MATRIX, projectionMatrix);
		terrainShader.setUniform(TerrainShader.TEXTURE_SAMPLER, 0);

		for (Renderable item : items.get(terrainShader)) { // DRAW TERRAIN
			if (item.isInitialized()) {
				Matrix4f viewMatrix = transformation.getViewMatrix(camera);
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				terrainShader.setUniform(TerrainShader.WORLD_MATRIX, modelViewMatrix);
				item.getMesh().render();
			}
		}
		terrainShader.unbind();
	}
	
	private void drawEntities(Matrix4f projectionMatrix) {
		entityShader.bind();
		entityShader.setUniform(TerrainShader.PROJECTION_MATRIX, projectionMatrix);
		entityShader.setUniform(TerrainShader.TEXTURE_SAMPLER, 0);
		for (Renderable item : items.get(entityShader)) { // DRAW ENTITIES
			if (item.isInitialized()) {
				Matrix4f viewMatrix = transformation.getViewMatrix(camera);
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				entityShader.setUniform(TerrainShader.WORLD_MATRIX, modelViewMatrix);
				entityShader.setUniform("texOffset", item.getTextureOffset());
				entityShader.setUniform("alpha", item.getAlpha());
				item.getMesh().render();
			}
		}
		entityShader.unbind();
	}

	public void addRenderable(Shader s, Renderable r) {
		if (items.containsKey(s))
			window.runInContext(() -> items.get(s).add(r));
	}

	public void removeRenderable(Shader s, Renderable r) {
		if (items.containsKey(s))
			window.runInContext(() -> items.get(s).remove(r));
	}

	public void finish() {
		window.runInContext(() -> {
			for (LinkedList<Renderable> l : items.values())
				for (Renderable item : l)
					item.getMesh().cleanUp();
		});
	}

	public Camera getCamera() {
		return camera;
	}

	public EntityShader getEntityShader() {
		return entityShader;
	}

	public TerrainShader getTerrainShader() {
		return terrainShader;
	}

}
