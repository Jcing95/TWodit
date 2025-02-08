package de.jcing.game;

import de.jcing.engine.opengl.Renderer;
import de.jcing.game.stages.MainStage;
import de.jcing.util.Timer;
import de.jcing.window.Window;

public class Game {

	private boolean isInitialized;

	MainStage mainstage;

	Timer timer;

	final Renderer renderer;

	final Window win;

	public Game(Window win, Renderer renderer) {
		this.renderer = renderer;
		this.win = win;
		isInitialized = false;
		win.runInContext(this::init);
	}

	private void init() {
		mainstage = new MainStage(renderer);

		//repeat tick method 20 times per second!
		timer = new Timer(this::tick).per(20).second().start();
		isInitialized = true;
	}

	public void tick() {
		mainstage.tick();
		//TODO: tick entities and manage game events here!
	}

	public boolean isInitialized() {
		return isInitialized;
	}

}
