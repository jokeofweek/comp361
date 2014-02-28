package comp361.server.network;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import comp361.server.GameServer;
import comp361.server.network.handlers.LoginPacketHandler;
import comp361.server.network.handlers.MessagePacketHandler;
import comp361.server.network.handlers.NewGameDescriptorPacketHandler;
import comp361.server.network.handlers.RegisterPacketHandler;
import comp361.server.network.handlers.ServerPacketHandler;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.shared.MessagePacket;

/**
 * This packet listener is responsible for mapping received packet types to
 * their appropriate {@link ServerPacketHandler} classes.
 */
public class ServerPacketListener extends Listener {

	private GameServer gameServer;

	// Store the packet handlers in hash maps based on the session type.
	private Map<SessionType, Map<Class, ServerPacketHandler>> sessionTypePacketHandlers;

	public ServerPacketListener(GameServer gameServer) {
		this.gameServer = gameServer;

		this.sessionTypePacketHandlers = new HashMap<SessionType, Map<Class, ServerPacketHandler>>(
				SessionType.values().length);

		this.sessionTypePacketHandlers.put(SessionType.ANONYMOUS,
				setupAnonymousPacketHandlers());
		this.sessionTypePacketHandlers.put(SessionType.LOBBY,
				setupLobbyPacketHandlers());
		this.sessionTypePacketHandlers.put(SessionType.GAME,
				setupGamePacketHandlers());
		this.sessionTypePacketHandlers.put(SessionType.DISCONNECTED,
				setupDisconnectedPacketHandlers());
	}

	/**
	 * @return a map which maps java classes to their appropriate packet
	 *         handlers which should be called when a session receives this
	 *         packet and is in an {@link SessionType#ANONYMOUS} state.
	 * 
	 */
	private Map<Class, ServerPacketHandler> setupAnonymousPacketHandlers() {
		Map<Class, ServerPacketHandler> handlers = new HashMap<Class, ServerPacketHandler>();
		// Associate all handlers here.
		handlers.put(RegisterPacket.class, new RegisterPacketHandler());
		handlers.put(LoginPacket.class, new LoginPacketHandler());
		return handlers;
	}

	/**
	 * @return a map which maps java classes to their appropriate packet
	 *         handlers which should be called when a session receives this
	 *         packet and is in an {@link SessionType#LOBBY} state.
	 * 
	 */
	private Map<Class, ServerPacketHandler> setupLobbyPacketHandlers() {
		Map<Class, ServerPacketHandler> handlers = new HashMap<Class, ServerPacketHandler>();
		handlers.put(MessagePacket.class, new MessagePacketHandler());
		handlers.put(NewGameDescriptorPacket.class, new NewGameDescriptorPacketHandler());
		return handlers;
	}

	/**
	 * @return a map which maps java classes to their appropriate packet
	 *         handlers which should be called when a session receives this
	 *         packet and is in an {@link SessionType#GAME} state.
	 * 
	 */
	private Map<Class, ServerPacketHandler> setupGamePacketHandlers() {
		Map<Class, ServerPacketHandler> handlers = new HashMap<Class, ServerPacketHandler>();
		return handlers;
	}

	/**
	 * @return a map which maps java classes to their appropriate packet
	 *         handlers which should be called when a session receives this
	 *         packet and is in an {@link SessionType#DISCONNECTED} state.
	 * 
	 */
	private Map<Class, ServerPacketHandler> setupDisconnectedPacketHandlers() {
		Map<Class, ServerPacketHandler> handlers = new HashMap<Class, ServerPacketHandler>();
		return handlers;
	}

	@Override
	public void received(Connection connection, Object object) {
		// Find the appropriate handler based on the session type.
		Session session = (Session) connection;
		Map<Class, ServerPacketHandler> handlers = sessionTypePacketHandlers
				.get(session.getSessionType());
		ServerPacketHandler handler = handlers.get(object.getClass());
		// System.out.println("Received " + object.getClass() + " from " + session);
		// Make sure we have a handler before calling
		if (handler != null) {
			handler.handle(session, gameServer, object);
		} else {
			// Call the super
			super.received(connection, object);
		}

	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);

		// Properly disconnect the session
		((Session) connection).disconnect();

		gameServer.getConsole().println("Socket disconnected.");
	}

}
