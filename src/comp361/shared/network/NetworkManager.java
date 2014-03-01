package comp361.shared.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import comp361.shared.data.GameDescriptor;
import comp361.shared.data.Player;
import comp361.shared.data.PlayerUpdateStatus;
import comp361.shared.data.Statistics;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GenericError;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.PlayerListPacket;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.server.RegisterError;
import comp361.shared.packets.shared.MessagePacket;

public class NetworkManager {

	/**
	 * This registers all objects which are going to be sent across the network
	 * with the {@link Kryo} serializer.
	 * 
	 * @param endPoint
	 *            either the client or server.
	 */
	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(MessagePacket.class);
		kryo.register(RegisterPacket.class);
		kryo.register(RegisterError.class);
		kryo.register(LoginPacket.class);
		kryo.register(LoginError.class);
		kryo.register(GenericError.class);
		kryo.register(PlayerListPacket.class);
		kryo.register(PlayerUpdatePacket.class);
		kryo.register(PlayerUpdateStatus.class);
		kryo.register(Statistics.class);
		kryo.register(Player.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(HashSet.class);
		kryo.register(GameDescriptor.class);
		kryo.register(NewGameDescriptorPacket.class);
		kryo.register(GameDescriptorCreatedPacket.class);
		kryo.register(GameDescriptorListPacket.class);
		kryo.register(GameDescriptorPlayerUpdatePacket.class);
		kryo.register(JoinGamePacket.class);
	}

	private NetworkManager() {
	}

}
