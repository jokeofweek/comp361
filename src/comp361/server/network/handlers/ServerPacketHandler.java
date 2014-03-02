package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.network.ServerPacketListener;
import comp361.server.session.Session;

/**
 * This class is called when the server receives an object of type K. Note that
 * there should only be one instance of each packet handler, so you shouldn't
 * save any state in the handler itself.
 * 
 * @param <K>
 *            The type of object that this should handle.
 */
public interface ServerPacketHandler<K> {

	/**
	 * Called by {@link ServerPacketListener} when a packet of type K arrives.
	 * 
	 * @param session
	 *            The session that sent the packet.
	 * @param gameServer
	 *            The gameServer object.
	 * @param packet
	 *            The packet.
	 */
	public void handle(Session session, GameServer gameServer, K packet);

}
