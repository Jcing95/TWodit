package de.jcing.engine.gl;

import java.util.LinkedList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.utillities.log.Log;
import de.jcing.window.OpenGLWindow;

public class Renderer {

	private static final Log log = new Log(Renderer.class);

	/**
	 * Field of View in Radians
	 */
	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private OpenGLWindow window;

	private TestShader shader;

	private Transformation transformation;

	private Camera camera;

	private LinkedList<Renderable> items;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		transformation = new Transformation();
		camera = new Camera();
		items = new LinkedList<>();

		camera.setPosition(0, 0, 5);
		camera.setRotation(-45f, 0, 0);

		window.runInContext(() -> init());
	}

	private void init() {
		try {
			log.debug("init shader");
			shader = new TestShader();
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");
			shader.createUniform("texture_sampler");
			

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
		shader.bind();
		// update transformation
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("texture_sampler", 0);

		for (Renderable item : items) {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
			shader.setUniform("worldMatrix", modelViewMatrix);
			// Draw the mesh
			item.getMesh().render();
		}
		shader.unbind();
	}
	
	public void addRenderable(Renderable r) {
		window.runInContext(() -> items.add(r));
	}

	public void removeRenderable(Renderable r) {
		window.runInContext(() -> items.remove(r));
	}

	public void finish() {
		window.runInContext(() -> {
			for (Renderable item : items)
				item.getMesh().cleanUp();
		});
	}
	
	public Camera getCamera() {
		return camera;
	}

}
