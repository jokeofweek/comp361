package comp361.client.network;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import comp361.client.GameClient;
import comp361.client.network.handlers.MessagePacketHandler;
import comp361.shared.packets.shared.MessagePacket;

/**
 * This class is responsible for mapping all packets that a client receives to
 * the appropriate {@link ClientPacketHandler} class.
 */
public class ClientPacketListener extends Listener {

	private Map<Class, ClientPacketHandler> handlers;
	private GameClient gameClient;

	public ClientPacketListener(GameClient gameClient) {
		this.gameClient = gameClient;
		this.handlers = setupPacketHandlers();
	}

	private Map<Class, ClientPacketHandler> setupPacketHandlers() {
		Map<Class, ClientPacketHandler> handlers = new HashMap<Class, ClientPacketHandler>();
		handlers.put(MessagePacket.class, new MessagePacketHandler());
		return handlers;
	}

	@Override
	public void received(Connection connection, Object object) {
		// Try to get the right handler.
		ClientPacketHandler handler = handlers.get(object.getClass());
		if (handler != null) {
			handler.handle(gameClient, object);
		} else {
			super.received(connection, object);
		}
	}
}
