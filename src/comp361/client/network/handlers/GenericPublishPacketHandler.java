package comp361.client.network.handlers;

import comp361.client.GameClient;

/**
 * This packet handler causes the client to notify all listeners of the received object.
 * This allows us to avoid having too many packet handlers which do the same thing.
 */
public class GenericPublishPacketHandler implements ClientPacketHandler<Object> {

	@Override
	public void handle(GameClient gameClient, Object object) {
		gameClient.publishMessage(object);
	}
}
