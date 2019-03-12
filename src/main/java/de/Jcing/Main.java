package de.jcing;

import de.jcing.engine.gl.Renderer;
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
	
	public static Renderer renderer;	
	
	public static void main(String[] args) {
		
		win = new OpenGLWindow();
		
		renderer = new Renderer(win);

		win.run();
		
//		//initialize Window and main menu
//		window = new Window();
//		mainMenu = new MainMenu();
//		
//		//Add Keyboard binding to exit game
//		KeyBoard.addBinding(KeyBoard.ONPRESS, (key) -> {
//			if(key == KeyEvent.VK_ESCAPE)
//				finish();
//		});
//		
//		//Start the main scene.		
	}
	
	public static void initGame() {
		game = new Game();
	}
	
	public static void finish() {
		renderer.finish();
		log.info("exit now");
		//stop all scenes
		Topic.stopAll();
		//finish window to dispose swing frame
		win.end();
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
