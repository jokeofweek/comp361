package comp361.server.network;

import comp361.server.session.Session;
import comp361.shared.network.ClientPacketType;
import comp361.shared.network.InputByteBuffer;

/**
 * This class represents a packet that was received by the server for a given
 * session.
 */
public class ServerReceivedPacket {

	private Session session;
	private ClientPacketType type;
	private InputByteBuffer buffer;

	public ServerReceivedPacket(Session session, ClientPacketType type,
			byte[] bytes) {
		super();
		this.session = session;
		this.type = type;
		this.buffer = new InputByteBuffer(bytes);
	}

	public ClientPacketType getType() {
		return type;
	}

	public InputByteBuffer getByteBuffer() {
		return buffer;
	}

	public Session getSession() {
		return session;
	}

}