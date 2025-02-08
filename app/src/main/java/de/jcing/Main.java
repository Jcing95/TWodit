package de.jcing;

import de.jcing.engine.image.BufferedImageLoader;
import de.jcing.engine.image.ImageLoader;
import de.jcing.engine.opengl.Renderer;
import de.jcing.game.Game;
import de.jcing.window.Window;

public class Main {

	public static final String RESOURCES = "src/main/resources/";

	private static Renderer renderer;
	private static Window win;
	private static Game game;
	
	public static void main(String[] args) {
		ImageLoader.init(new BufferedImageLoader());
		win = new Window();
		renderer = new Renderer(win);
		game = new Game(win, renderer);
		win.start();
	}
	
	public static void finish() {
		win.setRunning(false);
		renderer.finish();
	}
	
}
