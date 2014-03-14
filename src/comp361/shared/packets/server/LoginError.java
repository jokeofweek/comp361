package comp361.shared.packets.server;

public enum LoginError {
	NO_SUCH_ACCOUNT("No account exists with that name."),
	INVALID_CREDENTIALS("Those credentials were invalid. Please try again."),
	ACCOUNT_IN_USE("Someone is already using that account. Please try again."),
	LOAD_ERROR("An error occurred loading your account. Please try again later.");
	private String text;
	private LoginError(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
}
