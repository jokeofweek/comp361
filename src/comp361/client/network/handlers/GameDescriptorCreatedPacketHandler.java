package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;

public class GameDescriptorCreatedPacketHandler implements
		ClientPacketHandler<GameDescriptorCreatedPacket> {

	@Override
	public void handle(GameClient gameClient, GameDescriptorCreatedPacket object) {
		gameClient.getGameDescriptorManager().addDescriptor(object.descriptor);
		// TODO: Actually join the game if you created it.
	}

}
