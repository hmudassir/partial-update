package dev.portfolio.patch.error;

public class ProfileUpdateException extends RuntimeException {

	private final int status;

	public ProfileUpdateException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int status() {
		return status;
	}
}
