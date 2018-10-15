package de.Jcing.engine.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;

public class KeyBoard {
	
	
	public static final int ONTYPE = 10;
	public static final int ONPRESS = 20;
	public static final int ONRELEASE = 30;
	
	private static final HashMap<Integer, Boolean> pressedKeys = new HashMap<>();
	
	private static final Binding pressKey = (key) -> pressedKeys.put(key, true);
	private static final Binding releaseKey = (key) -> pressedKeys.put(key, false);
	
	public static final LinkedList<Binding> onType = new LinkedList<>();
	public static final LinkedList<Binding> onPress = new LinkedList<>();
	public static final LinkedList<Binding> onRelease = new LinkedList<>();
	
	private static final HashMap<Integer, LinkedList<Binding>> bindings = new HashMap<>();
	
	static {
		onPress.add(pressKey);
		onRelease.add(releaseKey);
		bindings.put(ONTYPE, onType);
		bindings.put(ONPRESS, onPress);
		bindings.put(ONRELEASE, onRelease);
	}
	
	public static final KeyListener keyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			for(Binding b : onType)
				b.onAction(e.getKeyCode());
		}

		@Override
		public void keyPressed(KeyEvent e) {
			for(Binding b : onPress)
				b.onAction(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			for(Binding b : onRelease)
				b.onAction(e.getKeyCode());
		}
	};
	
	public static void addBinding(int KEY, Binding binding) {
		if(bindings.containsKey(KEY))
			bindings.get(KEY).add(binding);
		else
			throw new IllegalArgumentException("invalid binding key! " + bindings.keySet());
	}
	
	public static boolean isPressed(int keyCode) {
		return pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode);
	}

}
