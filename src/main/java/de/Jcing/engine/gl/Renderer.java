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
	
	@SuppressWarnings("rawtypes")
	private HashMap[] modelViewMatrices = { new HashMap<>(), new HashMap<>() };
	int nextBufferIndex;
	private boolean swapBuffers;

	private int currentBufferIndex;

	private boolean buffersInitialized;

	public Renderer(Window win) {
		this.window = win;
		
		//transformation handles matrix calculations for 3D space.
		//it creates View and projectionmatrix needed to render gameitems at their current worldposition.
		transformation = new Transformation();
		camera = new Camera();
		items = new HashMap<>();

		camera.setPosition(0, 0, 5);
		camera.setRotation(-45f, 0, 0);
		
		//All OpenGL actions have to run in context of the window!
		window.getContext().run(() -> init());
	}

	private void init() {
		try {
			log.debug("initializing shaders");
			terrainShader = new TerrainShader();
			entityShader = new EntityShader();
			
			//items is a Shader-(List of Renderable) Map to render every item with corresponding shader.
			items.put(terrainShader, new LinkedList<>());
			items.put(entityShader, new LinkedList<>());

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("renderer initialized -> starting render loop");
		window.getContext().loop(() -> render());

	}

	public void render() {
		if (window.isResized()) {
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		// update transformation
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		
		//swap Matrix buffers to prevent movement of items while frame is rendered (black gaps, tearing)
		swapBuffers();
		if(!buffersInitialized)
			return;
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
				terrainShader.setUniform(TerrainShader.WORLD_MATRIX, getModelViewMatrix(item));
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
				entityShader.setUniform(TerrainShader.WORLD_MATRIX, getModelViewMatrix(item));
				entityShader.setUniform("texOffset", item.getTextureOffset());
				entityShader.setUniform("alpha", item.getAlpha());
				item.getMesh().render();
			}
		}
		entityShader.unbind();
	}

	public void addRenderable(Shader s, Renderable r) {
		if (items.containsKey(s))
			window.getContext().run(() -> items.get(s).add(r));
	}

	public void removeRenderable(Shader s, Renderable r) {
		if (items.containsKey(s))
			window.getContext().run(() -> items.get(s).remove(r));
	}

	public void finish() {
		window.getContext().run(() -> {
			for (LinkedList<Renderable> l : items.values())
				for (Renderable item : l)
					item.getMesh().cleanUp();
		});
	}
	
	private void swapBuffers() {
		if(swapBuffers) {
			currentBufferIndex = nextBufferIndex;
			nextBufferIndex = (nextBufferIndex+1) % modelViewMatrices.length;
			modelViewMatrices[nextBufferIndex].clear();
			swapBuffers = false;
		}
	}

	public void swapMatrixBuffer() {
		swapBuffers = true;
		if(!buffersInitialized)
			buffersInitialized = true;
	}
	
	@SuppressWarnings("unchecked")
	public void bufferWorldMatrix(Renderable item) {
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
		modelViewMatrices[nextBufferIndex].put(item,modelViewMatrix);
	}
	
	private Matrix4f getModelViewMatrix(Renderable item) {
		return (Matrix4f) modelViewMatrices[currentBufferIndex].get(item);
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
