package de.jcing;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

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
	
	static int vaoId;
	static int vboId;
	
	public static void main(String[] args) {
		
		win = new OpenGLWindow();
		
		win.runInContext(() -> {
			log.debug("init shader");
			shader = new TestShader();
			
			float[] vertices = new float[]{
					0.0f, 0.5f, 0.0f,
					-0.5f, -0.5f, 0.0f,
					0.5f, -0.5f, 0.0f
					};
			
			FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
			
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			
			vboId = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verticesBuffer, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
			// Unbind the VBO
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			// Unbind the VAO
			GL30.glBindVertexArray(0);
			MemoryUtil.memFree(verticesBuffer);
			GL30.glViewport(0, 0, 300, 300);
		});
		
		win.loopInContext(() -> {
			shader.bind();
			// Bind to the VAO
			GL30.glBindVertexArray(vaoId);
			GL30.glEnableVertexAttribArray(0);
			// Draw the vertices
			GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 3);
			// Restore state
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
