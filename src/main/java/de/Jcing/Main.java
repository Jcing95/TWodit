package de.Jcing;

import java.awt.event.KeyEvent;

import de.Jcing.engine.io.KeyBoard;
import de.Jcing.game.Game;
import de.Jcing.tasks.Clock;
import de.Jcing.window.Window;

public class Main {

	private static Game game;
	
	private static Window window;
	
	
	public static void main(String[] args) {	
		window = new Window();
		game = new Game();
		
		KeyBoard.addBinding(KeyBoard.ONPRESS, (key) -> {
			if(key == KeyEvent.VK_ESCAPE)
				finish();
		});
		
		
		Clock.start();
	}
	
	public static void finish() {	
		Clock.stop();
		
		window.finish();
	}
	
	public static Window getWindow() {
		return window;
	}
	
	public static Game getGame() {
		return game;
	}
	
	
}
