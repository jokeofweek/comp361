package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;

public class GameDescriptorCreatedPacketHandler implements
		ClientPacketHandler<GameDescriptorCreatedPacket> {

	@Override
	public void handle(GameClient gameClient, GameDescriptorCreatedPacket packet) {
		gameClient.getGameDescriptorManager().addGameDescriptor(packet.descriptor);

		// If you created the game, then we auto-join.
		if (packet.isCreator) {
			JoinGamePacket joinPacket = new JoinGamePacket();
			joinPacket.id = packet.descriptor.getId();
			gameClient.getClient().sendTCP(joinPacket);
		}
	}

}
