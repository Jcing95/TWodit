package de.jcing.engine.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyBoard {

	public static final int ONTYPE = 10;
	public static final int ONPRESS = 20;
	public static final int ONRELEASE = 30;

	private static final HashMap<Integer, Boolean> pressedKeys = new HashMap<>();

	public static final LinkedList<Binding> onType = new LinkedList<>();
	public static final LinkedList<Binding> onPress = new LinkedList<>();
	public static final LinkedList<Binding> onRelease = new LinkedList<>();

	private static final HashMap<Integer, LinkedList<Binding>> bindings = new HashMap<>();

	private static final HashSet<Integer> toggleable = new HashSet<>();
	private static final HashMap<Integer, Boolean> keyToggled = new HashMap<>();
	
	private KeyBoard() {};
	
	private static final Binding pressKey = (key) -> {
		pressedKeys.put(key, true);
		if (toggleable.contains(key)) {
			if (keyToggled.containsKey(key)) {
				if (!keyToggled.get(key)) {
					keyToggled.remove(key);
				}
			} else {
				keyToggled.put(key, true);
			}
		}
	};

	private static final Binding releaseKey = (key) -> {
		pressedKeys.put(key, false);
		if (toggleable.contains(key) && keyToggled.containsKey(key)) {
			keyToggled.put(key, false);
		}
	};

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
			for (Binding b : onType)
				b.onAction(e.getKeyCode());
		}

		@Override
		public void keyPressed(KeyEvent e) {
			for (Binding b : onPress)
				b.onAction(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			for (Binding b : onRelease)
				b.onAction(e.getKeyCode());
		}
	};
	
	public static final GLFWKeyCallbackI keyCallBack = new GLFWKeyCallbackI() {
		
		//TODO: implement key typed functionality for GLFW. somehow abstract callback and create IO submodule!
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			switch(action) {
			case GLFW.GLFW_RELEASE:
				for (Binding b : onRelease)
					b.onAction(key);
				break;
			case GLFW.GLFW_PRESS:
				for (Binding b : onPress)
					b.onAction(key);
				break;
			}
		}
	};
	
	public static void resetKey(int keyCode) {
		keyToggled.remove(keyCode);
	}

	public static void listenOnToggle(int keyCode) {
		toggleable.add(keyCode);
	}

	public static void unlistenToggle(int keyCode) {
		toggleable.add(keyCode);
		if (keyToggled.containsKey(keyCode))
			keyToggled.remove(keyCode);
	}

	public static void addBinding(int KEY, Binding binding) {
		if (bindings.containsKey(KEY))
			bindings.get(KEY).add(binding);
		else
			throw new IllegalArgumentException("invalid binding key! " + bindings.keySet().toString());
	}

	public static boolean isPressed(int keyCode) {
		return pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode);
	}

	public static boolean isToggled(int keyCode) {
		return keyToggled.containsKey(keyCode);
	}

}
