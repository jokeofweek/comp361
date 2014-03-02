package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;

public class GameDescriptorPlayerUpdatePacketHandler implements
		ClientPacketHandler<GameDescriptorPlayerUpdatePacket> {
	@Override
	public void handle(GameClient gameClient,
			GameDescriptorPlayerUpdatePacket packet) {
		// Send the message to the manager
		gameClient.getGameDescriptorManager().updateGameDescriptor(packet);
		// Dispatch the message
		gameClient.publishMessage(packet);
	}
}
