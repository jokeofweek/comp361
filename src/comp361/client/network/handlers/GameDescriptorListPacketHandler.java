package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorListPacket;

public class GameDescriptorListPacketHandler implements
		ClientPacketHandler<GameDescriptorListPacket> {

	@Override
	public void handle(GameClient gameClient, GameDescriptorListPacket packet) {
		// Update the data in the descriptor manager
		gameClient.getGameDescriptorManager().updateGameDescriptorList(packet);
	}

}
