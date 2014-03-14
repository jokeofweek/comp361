package comp361.shared.packets.server;

public enum RegisterError {
	ACCOUNT_ALREADY_EXISTS("An account already exists with that name."),
	SAVE_ERROR("An error occurred creating your account. Please try again later.");
	private String text;
	private RegisterError(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
}
