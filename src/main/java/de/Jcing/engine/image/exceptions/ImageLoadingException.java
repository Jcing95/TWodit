package de.jcing.engine.image.exceptions;

public class ImageLoadingException extends RuntimeException {

	private static final long serialVersionUID = 3336990833035383653L;

	public ImageLoadingException() {
	}
	
	public ImageLoadingException(String message) {
		super(message);
	}

}