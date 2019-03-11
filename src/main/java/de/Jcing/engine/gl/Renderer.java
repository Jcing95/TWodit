package de.jcing.engine.gl;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import de.jcing.utillities.log.Log;
import de.jcing.window.OpenGLWindow;

public class Renderer {
	
	private static final Log log = new Log(Renderer.class);
	
	private OpenGLWindow window;
	
	private TestShader shader;
	private Mesh testMesh;
	
	
    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;
    
    private Matrix4f projectionMatrix;

	public Renderer(OpenGLWindow win) {
		this.window = win;
		init();
	}
	
	private void init() {
		window.runInContext(() -> {
			float aspectRatio = (float) window.getWidth() / window.getHeight();
			
			projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
			    Z_NEAR, Z_FAR);
			
			log.debug("init shader");
			shader = new TestShader();
			
			float[] positions = new float[]{
			    -0.5f,  0.5f, -1.05f,
			    -0.5f, -0.5f, -1.05f,
			     0.5f, -0.5f, -1.05f,
			     0.5f,  0.5f, -1.05f,
			};
			
			float[] colours = new float[]{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
			};
			
			int[] indices = new int[]{
			    0, 1, 3, 3, 1, 2,
			};
			testMesh = new Mesh(positions, colours, indices);
			
			shader.createUniform("projectionMatrix");
			shader.setUniform("projectionMatrix", projectionMatrix);
			
			GL30.glViewport(0, 0, window.getWidth(), window.getHeight());

		});
		
		window.loopInContext(() -> {
			if (window.isResized()) {
				GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
				window.setResized(false);
			}
			shader.bind();
			// Draw the mesh
			GL30.glBindVertexArray(testMesh.getVaoId());
			GL30.glEnableVertexAttribArray(0);
			GL30.glEnableVertexAttribArray(1);
			GL30.glDrawElements(GL30.GL_TRIANGLES, testMesh.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);

			// Restore state
			GL30.glDisableVertexAttribArray(1);
			GL30.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
			
			shader.unbind();
		});
		
	}

	public void finish() {
		window.runInContext(() -> {			
			testMesh.cleanUp();
		});
	}
	
	
	
	
	
}
