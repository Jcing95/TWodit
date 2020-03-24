package de.jcing.window.gui.event;

public class MouseEvent {

	public static enum Action {
		PRESS, RELEASE;
	}

	private Action action;
	private float x, y;

	public MouseEvent(Action action, float x, float y) {
		this.action = action;
		this.x = x;
		this.y = y;
	}

	public Action action() {
		return action;
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}
}
