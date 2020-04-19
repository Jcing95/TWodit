package de.jcing.game;

import de.jcing.engine.opengl.Renderer;
import de.jcing.game.stages.MainStage;
import de.jcing.utillities.task.Task;
import de.jcing.window.Window;

public class Game {

	private boolean isInitialized;

	MainStage mainstage;

	Task tick;

	Renderer renderer;

	Window win;

	public Game(Window win, Renderer renderer) {
		this.renderer = renderer;
		this.win = win;
		isInitialized = false;
		win.getContext().run(() -> init());
	}

	private void init() {
		mainstage = new MainStage(renderer);

		//repeat tick method 20 times per second!
		tick = new Task(() -> tick()).name("Gametick").repeat(Task.perSecond(20)).start();
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
