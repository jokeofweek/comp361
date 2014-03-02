package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.shared.ChangeSeedPacket;

public class ChangeSeedPacketHandler implements
		ClientPacketHandler<ChangeSeedPacket> {
	@Override
	public void handle(GameClient gameClient, ChangeSeedPacket packet) {
		// Update the game descriptor
		gameClient.getGameDescriptorManager().updateGameSeed(packet.id,
				packet.seed);
		// Forward the message
		gameClient.publishMessage(packet);
	}
}
