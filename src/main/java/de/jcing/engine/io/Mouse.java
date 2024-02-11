package de.jcing.engine.io;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class Mouse {

	private final ArrayList<Runnable> onPress = new ArrayList<>();
	private final ArrayList<Runnable> onRelease = new ArrayList<>();

	private final DoubleBuffer xPosBuffer;
	private final DoubleBuffer yPosBuffer;

	private Mouse() {
	}

	public final GLFWMouseButtonCallbackI mouseButtonCallback = (window, button, action, mods) -> {
		switch (action) {
			case GLFW.GLFW_PRESS -> {
				for (Runnable r : onPress)
					r.run();
			}
			case GLFW.GLFW_RELEASE -> {
				for (Runnable b : onRelease)
					r.run();
			}
		}
	};

	public void update(int windowWidth, int windowHeight) {
		lx = x;
		ly = y;
		x = (float) (mouseX.get() / windowWidth);
		y = (float) (mouseY.get() / windowHeight);
	}

	public Vector2f getPos() {
		return new Vector2f(x, y);
	}

}
