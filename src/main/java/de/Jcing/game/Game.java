package de.jcing.game;

import de.jcing.engine.gl.Renderer;
import de.jcing.engine.window.Window;
import de.jcing.game.stages.MainStage;
import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Task;

public class Game {
	
	private static final Log LOG = new Log(Game.class);
	
	private boolean isInitialized;
	
	MainStage mainstage;
	
	Task tick;
	
	Renderer renderer;
	
	Window win;
		
	public Game (Window win, Renderer renderer) {
		this.renderer = renderer;
		this.win = win;
		isInitialized = false;
		win.getContext().run(() -> init());
	}
	
	private void init() {
		mainstage = new MainStage(renderer);
		new Task(() -> tick()).name("Gametick").repeat(Task.perSecond(20)).start();
		isInitialized = true;
	}
	
	public void tick() {
//		System.out.println("gametick");
		mainstage.tick();

	}

	public boolean isInitialized() {
		return isInitialized;
	}

	
}
