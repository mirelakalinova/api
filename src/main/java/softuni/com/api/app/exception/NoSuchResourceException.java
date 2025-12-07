package softuni.com.api.app.exception;



public class NoSuchResourceException extends RuntimeException {
	public NoSuchResourceException(String message) {
		super(message);
	}
}