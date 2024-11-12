package de.jcing.engine.io;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public static void onKeyEvent(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < keys.length) {
            keys[key] = (action != GLFW.GLFW_RELEASE);
        }
    }

    public static boolean isPressed(int keycode) {
        return keys[keycode];
    }

	public static void setCallbacks(long window) {
		GLFW.glfwSetKeyCallback(window, Keyboard::onKeyEvent);
	}
}

