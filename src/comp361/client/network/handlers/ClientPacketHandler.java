package comp361.client.network.handlers;

import comp361.client.GameClient;

/**
 * This class is called when a client receives an object of type K. Note that
 * there should only be one instance of each packet handler, so you shouldn't
 * save any state in the handler itself.
 * 
 * @param <K>
 *            The type of object that this should handle.
 */
public interface ClientPacketHandler<K> {

	/**
	 * Called when an object of type K is received.
	 * 
	 * @param gameClient
	 *            The client object.
	 * @param packet
	 *            The object.
	 */
	public void handle(GameClient gameClient, K packet);

}
