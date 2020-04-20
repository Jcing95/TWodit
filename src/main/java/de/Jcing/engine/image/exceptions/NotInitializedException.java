package de.jcing.engine.image.exceptions;

public class NotInitializedException extends RuntimeException {
	
	private static final long serialVersionUID = -5870672651992483446L;

	public NotInitializedException() {
		super();
	}
	
	public NotInitializedException(String message) {
		super(message);
	}
}
