package de.jcing;

import de.jcing.engine.gl.Renderer;
import de.jcing.game.Game;
import de.jcing.utillities.task.Topic;
import de.jcing.window.Window;

public class Main {

	public static final String RESSOURCES = "src/main/resources/";

	private static Game game;

	private static Window win;

	private static Renderer renderer;

	public static void main(String[] args) {

		win = new Window();

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
