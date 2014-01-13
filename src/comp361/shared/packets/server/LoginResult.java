package comp361.shared.packets.server;

public enum LoginResult {
	SUCCESS,
	NO_SUCH_ACCOUNT,
	INVALID_CREDENTIALS,
	ACCOUNT_IN_USE,
	LOAD_ERROR
}
