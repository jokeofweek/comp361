package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.data.GameDescriptorManager;
import comp361.server.session.Session;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;

public class NewGameDescriptorPacketHandler implements ServerPacketHandler<NewGameDescriptorPacket> {

	@Override
	public void handle(Session session, GameServer gameServer,
			NewGameDescriptorPacket packet) {
		// Create the new descriptor
		GameDescriptorManager manager = gameServer.getGameDescriptorManager();
		GameDescriptor descriptor = manager.createDescriptor(packet);
		
		// Send the ID to the creator
		GameDescriptorCreatedPacket creatorPacket = new GameDescriptorCreatedPacket();
		creatorPacket.descriptor = descriptor;
		creatorPacket.isCreator = true;
		session.sendTCP(creatorPacket);
		
		// Send the descriptor to all others
		GameDescriptorCreatedPacket nonCreatorPacket = new GameDescriptorCreatedPacket();
		nonCreatorPacket.descriptor = descriptor;
		nonCreatorPacket.isCreator = false;
		gameServer.getServer().sendToAllExceptTCP(session.getID(), nonCreatorPacket);
	}

}
