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
	
	private Task rotator;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		transformation = new Transformation();
		init();
	}

	private void init() {
		window.runInContext(() -> {

			log.debug("init shader");
			shader = new TestShader();
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");
			shader.createUniform("texture_sampler");
			Texture tex = new Texture("/gfx/terrain/cobble0.png");
			float[] positions = new float[] 
					{ -0.5f, 0.5f, -1.05f, 
					-0.5f, -0.5f, -1.05f,
					0.5f, -0.5f, -1.05f,
					0.5f, 0.5f, -1.05f, };

			float[] texCoords = new float[] {
					0.0f, 1.0f,
					0.0f, 0.0f,
					1.0f, 0.0f,
					1.0f, 1.0f,
					};

			int[] indices = new int[] { 0, 1, 3, 3, 1, 2, };
			testItem = new GameItem(new Mesh(positions, texCoords, indices, tex));
			testItem.setPosition(0, 0, -1);

			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			
			
			rotator = new Task(() -> {
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
			shader.setUniform("texture_sampler", 0);
			// Draw the mesh
			testItem.getMesh().render();

			shader.unbind();
		});
		
	}

	public void finish() {
		rotator.stop();
		window.runInContext(() -> {
			testItem.getMesh().cleanUp();
		});
	}

}
