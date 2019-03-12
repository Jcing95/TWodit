package de.jcing.engine.gl;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Task;
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

	private GameItem testItem;
	
	private float rot;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		transformation = new Transformation();
		init();
	}

	private void init() {
		window.runInContext(() -> {
			float aspectRatio = (float) window.getWidth() / window.getHeight();

//			projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
//			    Z_NEAR, Z_FAR);

			log.debug("init shader");
			shader = new TestShader();
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");

			float[] positions = new float[] { -0.5f, 0.5f, -1.05f, -0.5f, -0.5f, -1.05f, 0.5f, -0.5f, -1.05f, 0.5f,
					0.5f, -1.05f, };

			float[] colours = new float[] { 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f, };

			int[] indices = new int[] { 0, 1, 3, 3, 1, 2, };
			testItem = new GameItem(new Mesh(positions, colours, indices));
			testItem.setPosition(0, 0, -1);

			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			
			
			new Task(() -> {
				rot+= 0.2;
				testItem.setRotation(0, 0, rot);
			}).repeat(10).start();
		});

		window.loopInContext(() -> {
			if (window.isResized()) {
				GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
				window.setResized(false);
			}
			shader.bind();
			// update transformation
			Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
					Z_NEAR, Z_FAR);
			shader.setUniform("projectionMatrix", projectionMatrix);
			Matrix4f worldMatrix = transformation.getWorldMatrix(testItem.getPosition(), testItem.getRotation(),
					testItem.getScale());
			shader.setUniform("worldMatrix", worldMatrix);

			// Draw the mesh
			testItem.getMesh().render();

			shader.unbind();
		});
		
	}

	public void finish() {
		window.runInContext(() -> {
			testItem.getMesh().cleanUp();
		});
	}

}
