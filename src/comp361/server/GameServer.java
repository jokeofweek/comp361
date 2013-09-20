package comp361.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import comp361.server.console.Console;
import comp361.server.network.ServerPacketListener;
import comp361.server.session.Session;
import comp361.shared.network.NetworkManager;


public class GameServer {

	private Server server;
	private Console console;

	/**
	 * Gets a server application up and running.
	 * 
	 * @param console
	 *            The console to use.
	 * @param port
	 *            The port to use.
	 */
	public GameServer(final Console console, int port) {
		this.console = console;

		// Set up the server to create a new Session object on connection.
		server = new Server() {
			protected Connection newConnection() {
				console.println("Connection established.");
				return new Session();
			};
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		NetworkManager.register(server);

		// Add the standard packet listener.
		server.addListener(new ServerPacketListener(this));

		try {
			server.bind(port);
		} catch (IOException e) {
			console.println("Error occured while creating socket, shutting down...");
			e.printStackTrace();
			System.exit(1);
		}
		server.start();

		console.println("Listening for connections...");
	}

	/**
	 * @return the console associated with this server application.
	 */
	public Console getConsole() {
		return console;
	}

	/**
	 * @return the server for sending packets.
	 */
	public Server getServer() {
		return server;
	}
}
