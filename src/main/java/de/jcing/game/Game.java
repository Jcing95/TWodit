package de.jcing.game;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import de.jcing.engine.image.TextureAtlas;
import de.jcing.engine.image.generation.TextureBuilder;
import de.jcing.engine.opengl.Renderer;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.VertexData;
import de.jcing.engine.world.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Game {

	Stage mainstage;

	final Renderer renderer;
	private boolean shouldStop;
	private Thread gameThread;
	private static final long START_TIME = System.currentTimeMillis();

	public static final int time() {
		return (int)(System.currentTimeMillis() - START_TIME);
	}

	public Game(Renderer renderer) {
		this.renderer = renderer;
	}

	public void init() {
		try {
			TextureBuilder.BuiltTexture b = new TextureBuilder().addJSONInstructions("/player/player.json").build();
			TextureAtlas playerAtlas = b.getAtlas();
			Player player = new Player(new Mesh(playerAtlas, VertexData.createRectangle(0, 0, 0.1f, 1, 1, 0, 0,
					playerAtlas.getTextureWidth(), playerAtlas.getTextureHeight())), b);
			mainstage = new Stage(renderer,
					new TextureBuilder().addJSONInstructions("/terrain/grass/grass.json").build().getAtlas(), player);
			gameThread = Thread.ofPlatform().name("Gameloop").start(this::handleLoop);
		} catch (IOException e) {
			log.error("Could not initialize Game!", e);
		}
	}

	public Future<Void> stop() {
		shouldStop = true;
		return CompletableFuture.runAsync(() -> {
			try {
				gameThread.join();
			} catch (InterruptedException e) {
				gameThread.interrupt();
			}
		});
	}

	private void handleLoop() {
		final int targetUpdatesPerSecond = 20;
		final long targetTimeBetweenUpdates = 1_000_000_000 / targetUpdatesPerSecond; // Nanoseconds
		long lastUpdateTime = System.nanoTime();
		long lastMillis = System.currentTimeMillis();
		int ticks = 0;
		while (!shouldStop) {
			long now = System.nanoTime();
			long elapsedTime = now - lastUpdateTime;
			if (elapsedTime >= targetTimeBetweenUpdates) {
				tick();
				lastUpdateTime = now - (elapsedTime % targetTimeBetweenUpdates);
				ticks++;
			}
			// LOG the framerate once per second!
			if (System.currentTimeMillis() - lastMillis > 1000) {
				lastMillis = System.currentTimeMillis();
				log.info("{} TPS!", ticks);
				ticks = 0;
			}
			// wait nanotime until next frame should be drawn.
			long timeToNextUpdate = (lastUpdateTime + targetTimeBetweenUpdates) - System.nanoTime();
			if (timeToNextUpdate > 0) {
				try {
					Thread.sleep(timeToNextUpdate / 1_000_000, (int) (timeToNextUpdate % 1_000_000)); // Convert to ms
																										// and ns
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // Restore the interrupted status
				}
			}
		}
		log.debug("Game has finished.");
	}

	public void tick() {
		mainstage.tick();
	}
}
