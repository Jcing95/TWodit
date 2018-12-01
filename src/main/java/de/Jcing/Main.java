package de.Jcing;

import java.awt.event.KeyEvent;

import de.Jcing.engine.io.KeyBoard;
import de.Jcing.game.Game;
import de.Jcing.game.menu.MainMenu;
import de.Jcing.tasks.Clock;
import de.Jcing.window.Window;

public class Main {

	private static Game game;
	
	private static Window window;
	
	private static MainMenu mainMenu;
	
	public static final String RESSOURCES = "src/main/resources/";
	
	
	public static void main(String[] args) {
		//initialize Window and main menu
		window = new Window();
		mainMenu = new MainMenu();
		
		//Add Keyboard binding to exit game
		KeyBoard.addBinding(KeyBoard.ONPRESS, (key) -> {
			if(key == KeyEvent.VK_ESCAPE)
				finish();
		});
		
		//Start the main scene.
		Clock.start();
		
	}
	
	public static void initGame() {
		game = new Game();
	}
	
	public static void finish() {	
		//stop all scenes
		Clock.stopAll();
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
