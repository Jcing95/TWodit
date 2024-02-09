package de.jcing.game;

import java.io.IOException;

import de.jcing.engine.image.TextureAtlas;
import de.jcing.engine.image.generation.TextureFactory;
import de.jcing.engine.opengl.Renderer;
import de.jcing.engine.opengl.mesh.Mesh;
import de.jcing.engine.opengl.mesh.MeshFactory;
import de.jcing.game.stages.Stage;
import de.jcing.util.task.Task;
import de.jcing.window.Window;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Game {


	private boolean isInitialized;

	Stage mainstage;

	Task tick;

	final Renderer renderer;

	public Game(Renderer renderer) {
		this.renderer = renderer;
		isInitialized = false;
	}

	public void init() {
		try {
			TextureFactory.TextureBuilder b = new TextureFactory().addJSONInstructions("/player/player.json").build();
			TextureAtlas playerAtlas = b.getAtlas();
			Player player = new Player(new Mesh(playerAtlas, MeshFactory.createRectData(0, 0, 0.1f, 1, 1, 0,0,playerAtlas.getTextureWidth(), playerAtlas.getTextureHeight())), b);
			mainstage = new Stage(renderer, new TextureFactory().addJSONInstructions("/terrain/grass/grass.json").build().getAtlas(), player);
			// repeat tick method 20 times per second!
			tick = new Task(this::tick).name("Gametick").repeat(Task.perSecond(20)).start();
			isInitialized = true;
		} catch (IOException e) {
			log.error("Could not initialize Game!", e);
		}
	}

	public void tick() {
		mainstage.tick();
		// TODO: tick entities and manage game events here!
	}

	public boolean isInitialized() {
		return isInitialized;
	}

}
