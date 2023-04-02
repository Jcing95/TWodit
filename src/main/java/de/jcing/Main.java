package de.jcing;

import de.jcing.engine.image.BufferedImageLoader;
import de.jcing.engine.image.ImageLoader;
import de.jcing.engine.opengl.Renderer;
import de.jcing.game.Game;
import de.jcing.util.task.Topic;
import de.jcing.window.Window;

public class Main {

	public static final String RESSOURCES = "src/main/resources/";

	private static Game game;

	private static Renderer renderer;
	
	public static void main(String[] args) {
		
		ImageLoader.init(new BufferedImageLoader());

		Window win = new Window();

		renderer = new Renderer(win);
		game = new Game(win, renderer);

		win.run();

	}
	
	public static void finish() {
		renderer.finish();
		//stop all scenes
		Topic.stopAll();
	}

	public static Game getGame() {
		return game;
	}
	
}
