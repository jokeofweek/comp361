package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;

public class GameDescriptorReadyUpdatePacketHandler implements
		ClientPacketHandler<GameDescriptorReadyUpdatePacket> {
	@Override
	public void handle(GameClient gameClient,
			GameDescriptorReadyUpdatePacket packet) {
		gameClient.getGameDescriptorManager().updateGameDescriptor(packet);
	}
}
