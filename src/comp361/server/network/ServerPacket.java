package comp361.server.network;

import java.util.Collection;
import java.util.Collections;

import comp361.server.session.Session;
import comp361.shared.network.ServerPacketType;

/**
 * This is the base class for a packet that is getting sent from the server to
 * a/some/all client(s).
 */
public abstract class ServerPacket {

	/**
	 * @return the type of the packet.
	 */
	public abstract ServerPacketType getPacketType();

	/**
	 * @return the list of sessions that should receive this packet, or else
	 *         null/an empty list signifying that the packet should be sent to
	 *         everyone.
	 */
	public abstract Collection<Session> getRecipients();

	/**
	 * @return the bytes of the packet content.
	 */
	protected abstract byte[] getBytes();

	/**
	 * @return the raw bytes of the packet, including the packet type and
	 *         length. This should always be used when actually sending the
	 *         packet.
	 */
	public final byte[] getRawBytes() {
		byte[] data = getBytes();
		byte[] bytes = new byte[data.length + 5];
		bytes[0] = (byte) getPacketType().ordinal();
		bytes[1] = (byte) ((data.length >> 24) & 0xff);
		bytes[2] = (byte) ((data.length >> 16) & 0xff);
		bytes[3] = (byte) ((data.length >> 8) & 0xff);
		bytes[4] = (byte) (data.length & 0xff);
		System.arraycopy(data, 0, bytes, 5, data.length);
		return bytes;
	}

}