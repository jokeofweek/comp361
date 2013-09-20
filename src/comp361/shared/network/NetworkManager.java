package comp361.shared.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
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
	}

	private NetworkManager() {
	}

}
