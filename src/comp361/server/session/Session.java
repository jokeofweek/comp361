package comp361.server.session;

import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

import comp361.server.Server;

/**
 * This represents a client's session.
 */
public class Session {

	private Server server;
	private Socket socket;
	private SessionType sessionType;
	private Queue<byte[]> outgoingQueue;
	private volatile boolean connected;
	private SessionThread thread;

	/**
	 * Creates a new session.
	 * 
	 * @param server
	 *            The server to which the session is associated.
	 * @param socket
	 *            The session's socket.
	 * @throws SocketException
	 *             if an error occurs connecting the socket for the session
	 *             thread.
	 */
	public Session(Server server, Socket socket) throws SocketException {
		this.server = server;
		this.socket = socket;

		// Each session has it's own queue of outgoing bytes.
		this.outgoingQueue = new LinkedList<byte[]>();

		// Mark as connected
		this.sessionType = SessionType.CONNECTED;
		this.connected = true;

		// Start the thread
		this.thread = new SessionThread(this, this.socket);
		this.thread.start();
		
		server.getConsole().out().println("Session connection.");
	}

	/**
	 * @return the current session type.
	 */
	public SessionType getSessionType() {
		return sessionType;
	}

	/**
	 * Updates the session type.
	 * 
	 * @param sessionType
	 *            the new session type.
	 */
	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * @return the outgoing queue of this session - bytes placed in this queue
	 *         will be pushed out to the client via the {@link SessionThread}.
	 */
	public Queue<byte[]> getOutgoingQueue() {
		return outgoingQueue;
	}

	/**
	 * @return the server that this session is registered to.
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Disconnects the session from the server. After this point, we can assume
	 * the socket is closed by the {@link SessionThread} and should no longer
	 * send packets.
	 * 
	 * @throws IllegalStateException
	 *             if the session is already disconnected.
	 */
	public void disconnect() {
		if (this.sessionType == SessionType.DISCONNECTED) {
			throw new IllegalStateException(
					"Tried to disconnect a session that was already disconnected.");
		}

		this.server.getSessionManager().removeSession(this);
		this.sessionType = SessionType.DISCONNECTED;
		
		server.getConsole().out().println("Session disconnection.");
	}
}
