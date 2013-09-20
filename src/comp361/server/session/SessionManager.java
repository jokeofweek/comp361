package comp361.server.session;

import java.util.ArrayList;
import java.util.List;

import comp361.server.Server;

/**
 * This class is responsible for managing all sessions currently connected to
 * a server.
 */
public class SessionManager {

	private Server server;
	private List<Session> sessions;

	/**
	 * @param server
	 *            the server for which this manager is working.
	 */
	public SessionManager(Server server) {
		this.server = server;
		this.sessions = new ArrayList<Session>();
	}

	/**
	 * Registers a session with the session manager.
	 * 
	 * @param session
	 *            The session to add.
	 * @throws IllegalStateException
	 *             if the session is already registered.
	 */
	public void addSession(Session session) throws IllegalStateException {
		// Must lock on the sessions list in case multiple threads try to add a
		// session.
		synchronized (sessions) {
			if (this.sessions.contains(session)) {
				throw new IllegalStateException(
						"Tried to add a session that was already registered with the session manager.");
			}
			this.sessions.add(session);
		}
	}

	public void removeSession(Session session) {
		// Must lock on the sessions list in case multiple threads try to remove
		// a session.
		synchronized (sessions) {
			int index = this.sessions.indexOf(session);
			if (index < 0) {
				throw new IllegalStateException(
						"Tried to remove a session that wasn't registered with the session manager.");
			}

			this.sessions.remove(index);

			// Switch session to disconnected.
			session.setSessionType(SessionType.DISCONNECTED);
		}
	}

}
