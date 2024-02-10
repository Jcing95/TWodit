package de.jcing.window.gui.event;

public record MouseEvent(de.jcing.window.gui.event.MouseEvent.Action action, float x, float y) {

	public enum Action {
		PRESS, RELEASE
	}

}
