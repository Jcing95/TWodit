package de.jcing;

import java.awt.event.KeyEvent;

import de.jcing.engine.io.KeyBoard;
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
	
	private static MainMenu mainMenu;
	
	public static final String RESSOURCES = "src/main/resources/";
	
	
	public static void main(String[] args) {
		
		new OpenGLWindow().run();
		
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
