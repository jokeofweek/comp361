package comp361.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import comp361.server.data.AccountManager;
import comp361.server.data.GameDescriptorManager;
import comp361.server.data.SaveGameManager;
import comp361.server.data.store.AccountDataStore;
import comp361.server.logger.Logger;
import comp361.server.network.ServerPacketListener;
import comp361.server.session.Session;
import comp361.shared.data.GameDescriptor;
import comp361.shared.network.NetworkManager;
import comp361.shared.packets.server.SavedGameContainer;

public class GameServer {

	private Server server;
	private Logger logger;
	private AccountManager accountManager;
	private AccountDataStore accountDataStore;
	private GameDescriptorManager gameDescriptorManager;
	private SaveGameManager saveGameManager;

	/**
	 * Gets a server application up and running.
	 * 
	 * @param console
	 *            The console to use.
	 * @param port
	 *            The port to use.
	 * @param accountDataStore The account data store to use.
	 */
	public GameServer(final Logger logger, int port, AccountDataStore accountDataStore) {
		this.logger = logger;
		this.accountDataStore = accountDataStore;
		this.accountManager = new AccountManager();
		this.gameDescriptorManager = new GameDescriptorManager();
		this.saveGameManager = new SaveGameManager(this);

		// Set up the server. The newConnection callback is called every time a
		// connection is detected and allows us to wrap a connection object
		// in our own custom Session class.
		final GameServer self = this;
		server = new Server() {
			protected Connection newConnection() {
				logger.debug("Connection established.");
				Session s = new Session(self);
				s.addListener(new Listener() {
					@Override
					public void connected(Connection conn) {
						conn.setTimeout(5 * 60000);
					}
				});
				return s;
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
			logger.error("Error occured while creating socket, shutting down...");
			logger.debug(e.getMessage());
			System.exit(1);
		}
		server.start();

		logger.debug("Listening for connections on port " + port + "...");
	}

	/**
	 * @return the console associated with this server application.
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return the server for sending packets.
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * @return the data store for the accounts.
	 */
	public AccountDataStore getAccountDataStore() {
		return accountDataStore;
	}
	
	/**
	 * @return the account manager.
	 */
	public AccountManager getAccountManager() {
		return accountManager;
	}
	
	/**
	 * @return the manager for the {@link GameDescriptor} objects.
	 */
	public GameDescriptorManager getGameDescriptorManager() {
		return gameDescriptorManager;
	}
	
	public SaveGameManager getSaveGameManager() {
		return saveGameManager;
	}
	
}
