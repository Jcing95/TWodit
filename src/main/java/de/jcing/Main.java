package de.jcing;

import de.jcing.engine.opengl.Renderer;
import de.jcing.game.Game;
import de.jcing.window.Window;

public class Main {

	public static final String RESOURCES = "src/main/resources/";

	private static Renderer renderer;
	
	public static void main(String[] args) {
		renderer = new Renderer();
		Game game = new Game(renderer);
		Window win = new Window(renderer, game);
		win.run();
	}
	
	public static void finish() {
		renderer.finish();
	}
	
}
