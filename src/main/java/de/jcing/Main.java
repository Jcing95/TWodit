package de.jcing;

import de.jcing.engine.image.BufferedImageLoader;
import de.jcing.engine.image.ImageLoader;
import de.jcing.engine.opengl.Renderer;
import de.jcing.game.Game;
import de.jcing.util.task.Topic;
import de.jcing.window.Window;

public class Main {

	public static final String RESOURCES = "src/main/resources/";

	private static Renderer renderer;
	
	public static void main(String[] args) {
		ImageLoader.init(new BufferedImageLoader());
		Window win = new Window();
		renderer = new Renderer(win);
		Game game = new Game(win, renderer);
		win.run();
	}
	
	public static void finish() {
		renderer.finish();
		Topic.stopAll();
	}
	
}
