package comp361.client.network;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import comp361.client.GameClient;
import comp361.client.network.handlers.ChangeSeedPacketHandler;
import comp361.client.network.handlers.ClientPacketHandler;
import comp361.client.network.handlers.GameDescriptorCreatedPacketHandler;
import comp361.client.network.handlers.GameDescriptorListPacketHandler;
import comp361.client.network.handlers.GameDescriptorPlayerUpdatePacketHandler;
import comp361.client.network.handlers.GameDescriptorReadyUpdatePacketHandler;
import comp361.client.network.handlers.GenericPublishPacketHandler;
import comp361.client.network.handlers.PlayerListPacketHandler;
import comp361.client.network.handlers.PlayerUpdatePacketHandler;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;
import comp361.shared.packets.server.GenericError;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.PlayerListPacket;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.server.RegisterError;
import comp361.shared.packets.shared.ChangeSeedPacket;
import comp361.shared.packets.shared.MessagePacket;
import comp361.shared.packets.shared.SetupMessagePacket;

/**
 * This class is responsible for mapping all packets that a client receives to
 * the appropriate {@link ClientPacketHandler} class.
 */
@SuppressWarnings("rawtypes")
public class ClientPacketListener extends Listener {

	private Map<Class, ClientPacketHandler> handlers;
	private GameClient gameClient;

	public ClientPacketListener(GameClient gameClient) {
		this.gameClient = gameClient;
		this.handlers = setupPacketHandlers();
	}

	private Map<Class, ClientPacketHandler> setupPacketHandlers() {
		Map<Class, ClientPacketHandler> handlers = new HashMap<Class, ClientPacketHandler>();
		handlers.put(MessagePacket.class, new GenericPublishPacketHandler());
		handlers.put(RegisterError.class, new GenericPublishPacketHandler());
		handlers.put(LoginError.class, new GenericPublishPacketHandler());
		handlers.put(GenericError.class, new GenericPublishPacketHandler());
		handlers.put(PlayerUpdatePacket.class, new PlayerUpdatePacketHandler());
		handlers.put(PlayerListPacket.class, new PlayerListPacketHandler());
		handlers.put(GameDescriptorCreatedPacket.class,
				new GameDescriptorCreatedPacketHandler());
		handlers.put(GameDescriptorListPacket.class,
				new GameDescriptorListPacketHandler());
		handlers.put(GameDescriptorPlayerUpdatePacket.class,
				new GameDescriptorPlayerUpdatePacketHandler());
		handlers.put(ChangeSeedPacket.class, new ChangeSeedPacketHandler());
		handlers.put(GameDescriptorReadyUpdatePacket.class,
				new GameDescriptorReadyUpdatePacketHandler());
		handlers.put(SetupMessagePacket.class, new GenericPublishPacketHandler());

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
