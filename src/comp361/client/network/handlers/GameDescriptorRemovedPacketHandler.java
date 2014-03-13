package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.GameDescriptorRemovedPacket;

public class GameDescriptorRemovedPacketHandler implements
		ClientPacketHandler<GameDescriptorRemovedPacket> {

	@Override
	public void handle(GameClient gameClient, GameDescriptorRemovedPacket packet) {
		System.out.println("Removing!");
		gameClient.getGameDescriptorManager().removeGameDescriptor(packet.id);
	}

}
