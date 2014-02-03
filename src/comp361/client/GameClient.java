package comp361.client;

import java.io.IOException;
import java.util.Observable;

import com.esotericsoftware.kryonet.Client;

import comp361.client.data.PlayerManager;
import comp361.client.network.ClientPacketListener;
import comp361.shared.network.NetworkManager;

public class GameClient extends Observable {

	private Client client;
	private PlayerManager playerManager;
	
	/**
	 * Creates a new client for a given host and port.
	 * 
	 * @param host
	 *            the host.
	 * @param port
	 *            the port.
	 * @throws IOException
	 *             if any error occurs while connecting.
	 */
	public GameClient(String host, int port) throws IOException {
		this.client = new Client();
		client.start();

		// Create the player manager
		this.playerManager = new PlayerManager();
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		NetworkManager.register(client);

		client.addListener(new ClientPacketListener(this));

		client.connect(5000 /* timeout */, host, port);
		
	}

	/**
	 * @return the client for sending packets.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Publishes a message to all listening objects.
	 * 
	 * @param message
	 *            the message to publish.
	 */
	public void publishMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
}
