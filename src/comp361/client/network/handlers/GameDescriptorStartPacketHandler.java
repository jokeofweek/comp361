package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorStartPacket;

public class GameDescriptorStartPacketHandler implements
		ClientPacketHandler<GameDescriptorStartPacket> {
	@Override
	public void handle(GameClient gameClient, GameDescriptorStartPacket packet) {
		gameClient.getGameDescriptorManager().updateGameDescriptor(packet);

	}
}
