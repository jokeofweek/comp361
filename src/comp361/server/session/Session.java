package comp361.server.session;

import com.esotericsoftware.kryonet.Connection;

public class Session extends Connection {

	private SessionType sessionType;

	public Session() {
		// Set the session type to anonymous at connection time.
		this.sessionType = SessionType.ANONYMOUS;
	}

	/**
	 * Update the session type.
	 * 
	 * @param sessionType
	 *            the new type of the session.
	 */
	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * @return the current session type.
	 */
	public SessionType getSessionType() {
		return sessionType;
	}

	/**
	 * Effectively disconnects the session, changing the session type and
	 * performing any required processing.
	 */
	public void disconnect() {
		// Update the session type.
		this.setSessionType(SessionType.DISCONNECTED);
	}
}
