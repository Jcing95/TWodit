package de.jcing.engine.opengl;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcing.engine.opengl.mesh.Sprite;
import de.jcing.engine.opengl.shaders.EntityShader;
import de.jcing.engine.opengl.shaders.Shader;
import de.jcing.engine.opengl.shaders.TerrainShader;
import de.jcing.window.Window;

public class Renderer {

	private static final Logger LOG = LoggerFactory.getLogger(Renderer.class);

	/**
	 * Field of View in Radians
	 */
	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private TerrainShader terrainShader;
	private EntityShader entityShader;

	private final Transformation transformation;

	private final Camera camera;

	private final HashMap<Shader, ArrayList<Sprite>> items;

	public Renderer() {
		// transformation handles matrix calculations for 3D space.
		// it creates View and projectionmatrix needed to render gameitems at their
		// current worldposition.
		transformation = new Transformation();
		camera = new Camera();
		items = new HashMap<>();

		camera.setPosition(0, 0, 10);
		camera.setRotation(0, 0, 0);
	}

	public void init(Window window) {
		try {
			LOG.debug("initializing shaders");
			terrainShader = new TerrainShader();
			entityShader = new EntityShader();

			// items is a Shader-(List of Renderable) Map to render every item with
			// corresponding shader.
			items.put(terrainShader, new ArrayList<>());
			items.put(entityShader, new ArrayList<>());

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());

		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.debug("renderer initialized -> starting render loop");
	}

	public void render(Window window) {
		if (window.isResized()) {
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		// update transformation
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);

		drawTerrain(projectionMatrix);
		drawEntities(projectionMatrix);

		// TODO: DRAW GUI HERE
	}

	private void drawTerrain(Matrix4f projectionMatrix) {
		terrainShader.bind();
		terrainShader.setUniform(TerrainShader.PROJECTION_MATRIX, projectionMatrix);
		terrainShader.setUniform(TerrainShader.TEXTURE_SAMPLER, 0);

		for (Sprite item : items.get(terrainShader)) { // DRAW TERRAIN
			Matrix4f mat = getModelViewMatrix(item);
			if (mat != null) // TODO: check why mat can be null!
				terrainShader.setUniform(TerrainShader.WORLD_MATRIX, mat);
			item.getMesh().render();
		}
		terrainShader.unbind();
	}

	private void drawEntities(Matrix4f projectionMatrix) {
		entityShader.bind();
		entityShader.setUniform(TerrainShader.PROJECTION_MATRIX, projectionMatrix);
		entityShader.setUniform(TerrainShader.TEXTURE_SAMPLER, 0);
		for (Sprite item : items.get(entityShader)) { // DRAW ENTITIES
			entityShader.setUniform(TerrainShader.WORLD_MATRIX, getModelViewMatrix(item));
			entityShader.setUniform("texOffset", item.getTextureOffset());
			entityShader.setUniform("alpha", item.getAlpha());
			item.getMesh().render();
		}
		entityShader.unbind();
	}

	public void addRenderable(Shader s, Sprite r) {
		if (items.containsKey(s))
			items.get(s).add(r);
	}

	public void removeRenderable(Shader s, Sprite r) {
		if (items.containsKey(s))
			items.get(s).remove(r);
	}

	public void finish() {
		for (ArrayList<Sprite> l : items.values())
			for (Sprite item : l)
				item.getMesh().cleanUp();
	}

	private Matrix4f getModelViewMatrix(Sprite item) {
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		return transformation.getModelViewMatrix(item, viewMatrix);
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
