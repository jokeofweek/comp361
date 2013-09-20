package comp361.server.session;

/**
 * Represents the state of a given session, allowing us to only handle the
 * appropriate packets.
 */
public enum SessionType {
	CONNECTED, LOGGED_IN, IN_GAME, DISCONNECTED
}
