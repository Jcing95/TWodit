package de.jcing.engine.gl;

import java.util.LinkedList;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import de.jcing.engine.io.KeyBoard;
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
	
	private static final float size = 0.5f;

	private OpenGLWindow window;

	private TestShader shader;

	private Transformation transformation;
	
	private float rot;
	
	private Task rotator;
	
	private Camera camera;
	
	private LinkedList<GameItem> items;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		transformation = new Transformation();
		camera = new Camera();
		new Task(() -> {
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_W))
				camera.movePosition(0, 0.1f, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_A))
				camera.movePosition(-0.1f, 0, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_S))
				camera.movePosition(0, -0.1f, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_D))
				camera.movePosition(0.1f, 0, 0);
		}).repeat(Task.perSecond(20)).start();
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
					{ -size, size, -1.05f, 
					-size, -size, -1.05f,
					size, -size, -1.05f,
					size, size, -1.05f, };

			float[] texCoords = new float[] {
					0.0f, 1.0f,
					0.0f, 0.0f,
					1.0f, 0.0f,
					1.0f, 1.0f,
					};

			int[] indices = new int[] { 0, 1, 3, 3, 1, 2, };
			Mesh mesh = new Mesh(positions, texCoords, indices, tex);
			GameItem item1 = new GameItem(mesh);
			item1.setPosition(-1, -1, -1);
			GameItem item2 = new GameItem(mesh);
			item2.setPosition(-1, 0, -1);
			GameItem item3 = new GameItem(mesh);
			item3.setPosition(0, -1, -1);
			GameItem item4 = new GameItem(mesh);
			item4.setPosition(0, 0, -1);
			GameItem item5 = new GameItem(mesh);
			item5.setPosition(1, 0, -1);
			GameItem item6 = new GameItem(mesh);
			item6.setPosition(0, 1, -1);
			GameItem item7 = new GameItem(mesh);
			item7.setPosition(1, 1, -1);
			GameItem item8 = new GameItem(mesh);
//			item8.setPosition(1, 1, -1);
			items = new LinkedList<>();
			items.add(item1);
			items.add(item2);
			items.add(item3);
			items.add(item4);
			items.add(item5);
			items.add(item6);
			items.add(item7);
			items.add(item8);
			
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
			
			
			rotator = new Task(() -> {
				rot += 0.2;
				camera.setRotation(0, 0, rot);
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
			Matrix4f viewMatrix = transformation.getViewMatrix(camera);
			shader.setUniform("projectionMatrix", projectionMatrix);
			shader.setUniform("texture_sampler", 0);
			
			for(GameItem item : items) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				shader.setUniform("worldMatrix", modelViewMatrix);
				// Draw the mesh
				item.getMesh().render();
			}
			shader.unbind();
		});
		
	}

	public void finish() {
		rotator.stop();
		window.runInContext(() -> {
			for(GameItem item : items)
				item.getMesh().cleanUp();
		});
	}

}
