package comp361.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import comp361.server.console.Console;
import comp361.server.network.ServerPacket;
import comp361.server.network.ServerReceivedPacket;
import comp361.server.session.Session;
import comp361.server.session.SessionManager;
import comp361.shared.network.ServerPacketType;

/**
 * This class acts as the base entry point for new clients. It initializes our
 * server and then waits for connections, creating sessions for each new
 * connection.
 */
public class Server {

	private Console console;
	private ServerSocket serverSocket;
	private SessionManager sessionManager;
	private BlockingQueue<ServerReceivedPacket> incomingPackets;
	private BlockingQueue<ServerPacket> outgoingPackets;

	/**
	 * @param console
	 *            The server's console.
	 * @param port
	 *            The port to listen on.
	 */
	public Server(Console console, int port) {
		this.console = console;

		// Try to set up, exiting if a failure occurs.
		if (!this.setUp(port)) {
			console.out().println("Shutting down, error in setup...");
			System.exit(1);
		}
	}

	/**
	 * Performs any internal setup.
	 * 
	 * @param port
	 * @return a boolean dictating whether setup was succesful. Note that if
	 *         false is returned, the server will exit.
	 */
	private boolean setUp(int port) {

		console.out().println("Creating session manager...");
		this.sessionManager = new SessionManager(this);

		console.out().println("Setting up packet queues...");
		this.incomingPackets = new LinkedBlockingQueue<ServerReceivedPacket>();
		this.outgoingPackets = new LinkedBlockingQueue<ServerPacket>();

		// Try to open the server
		try {
			console.out().println("Opening socket on port " + port + "...");
			this.serverSocket = new ServerSocket(port);
			console.out().println("Server is now listening!");
		} catch (Exception e) {
			// On error print the stack trace and error out.
			e.printStackTrace(console.out());
			return false;
		}

		// Success!
		return true;
	}

	/**
	 * Starts running the server's socket thread.
	 */
	public void run() {
		Socket socket = null;
		// Accept connections indefinitely.
		while (true) {
			try {
				// Note that accept blocks until we have a connection
				// request.
				socket = serverSocket.accept();
				Session session = new Session(this, socket);
				// Add the session to the session manager.
				this.sessionManager.addSession(session);
			} catch (IOException e) {
				// Write error to console.
				console.out().println("Error accepting socket connection.");
				e.printStackTrace(console.out());
			}
		}
	}

	/**
	 * @return the server's console.
	 */
	public Console getConsole() {
		return console;
	}

	/**
	 * @return the server's session manager.
	 */
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	/**
	 * Pushes a packet to the queue of incoming packets that must be processed.
	 * 
	 * @param packet
	 *            the packet.
	 */
	public void receivePacket(ServerReceivedPacket packet) {
		this.incomingPackets.add(packet);
	}

	/**
	 * Pushes a packet to the queue of outgoing packets.
	 * 
	 * @param serverPacket
	 *            the packet to send.
	 */
	public void sendPacket(ServerPacket serverPacket) {
		this.outgoingPackets.add(serverPacket);
	}

}
