package de.jcing;

import org.lwjgl.opengl.GL30;

import de.jcing.engine.gl.Mesh;
import de.jcing.engine.gl.TestShader;
import de.jcing.game.Game;
import de.jcing.game.menu.MainMenu;
import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Topic;
import de.jcing.window.OpenGLWindow;
import de.jcing.window.Window;

public class Main {
	
	private static final Log log = new Log(Main.class);
	
	private static Game game;
	
	private static Window window;
	
	private static OpenGLWindow win;
	
	private static MainMenu mainMenu;
	
	public static final String RESSOURCES = "src/main/resources/";
	
	static TestShader shader;
	static Mesh testMesh;
	
	static int vaoId;
	static int vboId;
	
	public static void main(String[] args) {
		
		win = new OpenGLWindow();
		
		win.runInContext(() -> {
			log.debug("init shader");
			shader = new TestShader();
			
			float[] positions = new float[]{
			    -0.5f,  0.5f, 0.0f,
			    -0.5f, -0.5f, 0.0f,
			     0.5f, -0.5f, 0.0f,
			     0.5f,  0.5f, 0.0f,
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
			GL30.glViewport(0, 0, 300, 300);
		});
		
		win.loopInContext(() -> {
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

		win.run();
		
		//initialize Window and main menu
//		window = new Window();
//		mainMenu = new MainMenu();
//		
//		//Add Keyboard binding to exit game
//		KeyBoard.addBinding(KeyBoard.ONPRESS, (key) -> {
//			if(key == KeyEvent.VK_ESCAPE)
//				finish();
//		});
		
		//Start the main scene.		
	}
	
	public static void initGame() {
		game = new Game();
	}
	
	public static void finish() {
		win.runInContext(() -> {			
			GL30.glDisableVertexAttribArray(0);
			// Delete the VBO
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glDeleteBuffers(vboId);
			// Delete the VAO
			GL30.glBindVertexArray(0);
			GL30.glDeleteVertexArrays(vaoId);
		});
		log.info("exit now");
		//stop all scenes
		Topic.stopAll();
		//finish window to dispose swing frame
		window.finish();
	}
	
	public static MainMenu getMainMenu() {
		return mainMenu;
	}
	
	public static Window getWindow() {
		return window;
	}
	
	public static Game getGame() {
		return game;
	}
	
	
}
