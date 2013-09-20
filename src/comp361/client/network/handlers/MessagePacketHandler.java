package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.client.network.ClientPacketHandler;
import comp361.shared.packets.shared.MessagePacket;

/**
 * This class simply publishes the message to the game client, which should add
 * it to the lobby window.
 */
public class MessagePacketHandler implements ClientPacketHandler<MessagePacket> {

	@Override
	public void handle(GameClient gameClient, MessagePacket object) {
		gameClient.publishMessage(object);
	}

}
