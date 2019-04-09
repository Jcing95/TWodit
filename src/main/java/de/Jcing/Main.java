package de.jcing;

import de.jcing.engine.gl.Renderer;
import de.jcing.engine.window.SwingWindow;
import de.jcing.engine.window.Window;
import de.jcing.game.Game;
import de.jcing.game.menu.MainMenu;
import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Topic;

public class Main {
	
	private static final Log log = new Log(Main.class);
	
	private static Game game;
	
	private static SwingWindow window;
	
	private static Window win;
	
	private static MainMenu mainMenu;
	
	public static final String RESSOURCES = "src/main/resources/";
	
	public static Renderer renderer;	
	
	public static void main(String[] args) {
//		
		win = new Window();
		
		renderer = new Renderer(win);
		initGame();

		win.run();
		
	}
	
	
	public static void initGame() {
		game = new Game(win, renderer);
	}
	
	public static void finish() {
		renderer.finish();
		//stop all scenes
		Topic.stopAll();
	}
	
	public static MainMenu getMainMenu() {
		return mainMenu;
	}
	
	public static SwingWindow getWindow() {
		return window;
	}
	
	public static Game getGame() {
		return game;
	}
	
	
}
