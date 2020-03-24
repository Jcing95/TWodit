package de.jcing.engine.io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class Mouse {

	public static float x, y;
	public static float lx, ly;

	public static final Object BLOCKER = Mouse.class;

	public static HashMap<Integer, Boolean> keys = new HashMap<>();

	public static final int ONPRESS = 0;
	public static final int ONRELEASE = 1;
	public static final int ONCLICK = 2;

	public static final int ONENTER = 10;
	public static final int ONEXIT = 11;

	public static final int ONMOVE = 20;
	public static final int ONDRAG = 21;

	public static final int ONWHEEL = 30;

	//using ArrayList for faster iteration
	private static final ArrayList<Binding> onPress = new ArrayList<>();
	private static final ArrayList<Binding> onRelease = new ArrayList<>();
	private static final ArrayList<Binding> onClick = new ArrayList<>();
	private static final ArrayList<Binding> onEnter = new ArrayList<>();
	private static final ArrayList<Binding> onExit = new ArrayList<>();
	private static final ArrayList<Binding> onMove = new ArrayList<>();
	private static final ArrayList<Binding> onDrag = new ArrayList<>();
	private static final ArrayList<Binding> onWheel = new ArrayList<>();

	public static final int LEFT = MouseEvent.BUTTON1;
	public static final int RIGHT = MouseEvent.BUTTON2;

	private static final DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
	private static final DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);

	private static final HashMap<Integer, ArrayList<Binding>> bindings = new HashMap<>();

	static {
		synchronized (BLOCKER) {
			bindings.put(ONPRESS, onPress);
			bindings.put(ONRELEASE, onRelease);
			bindings.put(ONCLICK, onClick);
			bindings.put(ONENTER, onEnter);
			bindings.put(ONEXIT, onExit);
			bindings.put(ONMOVE, onMove);
			bindings.put(ONDRAG, onDrag);
			bindings.put(ONWHEEL, onWheel);
		}
	}

	private Mouse() {
	};

	public static final MouseListener mouseListener = new MouseListener() {

		// TODO: check bindings for illegal modifications.

		@Override
		public void mouseClicked(MouseEvent e) {
			synchronized (BLOCKER) {
				for (Binding b : onClick)
					b.onAction(e.getButton());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			synchronized (BLOCKER) {
				keys.put(e.getButton(), true);
				for (Binding b : onPress)
					b.onAction(e.getButton());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			synchronized (BLOCKER) {
				keys.put(e.getButton(), false);
				for (Binding b : onRelease)
					b.onAction(e.getButton());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			synchronized (BLOCKER) {
				for (Binding b : onEnter)
					b.onAction(e.getButton());
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			synchronized (BLOCKER) {
				for (Binding b : onExit)
					b.onAction(e.getButton());
			}
		}

	};

	public static final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent e) {
			lx = x;
			x = e.getX();
			ly = y;
			y = e.getY();
			synchronized (BLOCKER) {
				for (Binding b : onDrag)
					b.onAction(e.getButton());
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lx = x;
			x = e.getX();
			ly = y;
			y = e.getY();
			synchronized (BLOCKER) {
				for (Binding b : onMove)
					b.onAction(e.getButton());
			}
		}
	};

	public static final MouseWheelListener mouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			for (Binding b : onWheel)
				b.onAction(e.getWheelRotation());
		}
	};

	public static final GLFWMouseButtonCallbackI mouseButtonCallback = new GLFWMouseButtonCallbackI() {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			switch (action) {
				case GLFW.GLFW_PRESS:
					for (Binding b : onPress)
						b.onAction(button);
					break;
				case GLFW.GLFW_RELEASE:
					for (Binding b : onRelease)
						b.onAction(button);
					break;
			}
		}
	};

	public static Binding addBinding(int KEY, Binding binding) {
		synchronized (BLOCKER) {
			if (bindings.containsKey(KEY))
				bindings.get(KEY).add(binding);
			else
				throw new IllegalArgumentException("invalid binding key!");
		}
		return binding;
	}

	public static void update(int windowWidth, int windowHeight) {
		lx = x;
		ly = y;
		x = (float) (mouseX.get() / windowWidth);
		y = (float) (mouseY.get() / windowHeight);
	}

	public static DoubleBuffer getXBuffer() {
		mouseX.clear();
		return mouseX;
	}

	public static DoubleBuffer getYBuffer() {
		mouseY.clear();
		return mouseY;
	}

	public static float getX() {
		return x;
	}

	public static float getY() {
		return y;
	}

	public static float getLastX() {
		return lx;
	}

	public static float getLastY() {
		return ly;
	}

	public static void removeBinding(Binding b) {
		synchronized (BLOCKER) {
			for (ArrayList<Binding> list : bindings.values()) {
				list.remove(b);
			}
		}
	}

	public static Vector2f getPos() {
		return new Vector2f(x, y);
	}

}
