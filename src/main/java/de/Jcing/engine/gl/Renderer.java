package de.jcing.engine.gl;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import de.jcing.engine.gl.mesh.Renderable;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.texture.TextureAtlas;
import de.jcing.engine.world.Chunk;
import de.jcing.image.MultiImage;
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
	
	private static final float DEFAULT_SPEED = 0.2f;
	
	private static final float SHIFT_MULT = 2.5f;
	
	private OpenGLWindow window;

	private TestShader shader;

	private Transformation transformation;
	
	private Camera camera;
	
	private LinkedList<Renderable> items;
	
	private Chunk[][] testChunk;
	
	private Animation playerAnim;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		transformation = new Transformation();
		camera = new Camera();
		camera.setPosition(0, 0, 5);
		camera.setRotation(-45f, 0, 0);
		new Task(() -> {
			float speed = DEFAULT_SPEED;
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
				speed *= SHIFT_MULT;
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_W))
				camera.movePosition(0, speed, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_A))
				camera.movePosition(-speed, 0, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_S))
				camera.movePosition(0, -speed, 0);
			if(KeyBoard.isPressed(GLFW.GLFW_KEY_D))
				camera.movePosition(speed, 0, 0);
		}).repeat(Task.perSecond(20)).start();
		init();
	}

	private void init() {
		window.runInContext(() -> {

			try {
				log.debug("init shader");
				shader = new TestShader();
				shader.createUniform("projectionMatrix");
				shader.createUniform("worldMatrix");
				shader.createUniform("texture_sampler");
				MultiImage grass = new MultiImage("gfx/terrain/grass");
				BufferedImage[] imgs = new BufferedImage[16];
				for(int i = 0; i < imgs.length; i++) {
					grass.seed(i);
					imgs[i] = grass.get();
				}
				TextureAtlas tex = new TextureAtlas(imgs);
				log.debug("tex: " + tex.getSubTexturesPerSide() + " w:" + tex.getSubTextureSideLength() + " total:" + tex.getSubTextureTotalCount());
				items = new LinkedList<>();
				testChunk = new Chunk[20][20];
				for(int x = -10; x < 10; x++)
					for(int y = -10; y < 10; y++)
						items.add(new Chunk(x,y,tex));
//						testChunk[x][y] = 
				
				GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
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
			
			for(Renderable item : items) {
				Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
				shader.setUniform("worldMatrix", modelViewMatrix);
				// Draw the mesh
				item.getMesh().render();
			}
			shader.unbind();
		});
		
	}

	public void finish() {
		window.runInContext(() -> {
			for(Renderable item : items)
				item.getMesh().cleanUp();
		});
	}

}
