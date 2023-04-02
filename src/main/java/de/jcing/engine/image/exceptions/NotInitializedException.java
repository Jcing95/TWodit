package de.jcing.engine.image.exceptions;

public class NotInitializedException extends RuntimeException {

	public NotInitializedException() {
		super();
	}
	
	public NotInitializedException(String message) {
		super(message);
	}
}
