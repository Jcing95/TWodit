package de.jcing;

import java.util.concurrent.ExecutionException;

import de.jcing.engine.opengl.Renderer;
import de.jcing.game.Game;
import de.jcing.window.Window;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static final String RESOURCES = "src/main/resources/";

	private static Renderer renderer;
	private static Window win;
	private static Game game;

	public static void main(String[] args) {
		renderer = new Renderer();
		game = new Game(renderer);
		win = new Window(renderer, game);
		win.run();
	}

	public static void finish() {
		Thread.ofVirtual().name("stopper").start(() -> {
			log.debug("Stopping...");
			try {
				var gameStop = game.stop();
				var winStop = win.stop();
				gameStop.get();
				winStop.get();
			} catch (InterruptedException | ExecutionException e) {
				log.error("something went wrong...", e);
			}
		});
	}

}
