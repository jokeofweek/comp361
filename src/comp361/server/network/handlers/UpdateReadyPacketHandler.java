package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;

public class UpdateReadyPacketHandler implements
		ServerPacketHandler<UpdateReadyPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			UpdateReadyPacket packet) {
		// Update the game internally.
		gameServer.getGameDescriptorManager().setReadyStatus(
				session.getGameDescriptorId(), session.getAccount().getName(),
				packet.ready);

		if (gameServer.getGameDescriptorManager().canStart(
				session.getGameDescriptorId())) {
			// TODO Handle all players being ready
		}
		
		System.out.println("Received!");

		// Notify the players
		GameDescriptorReadyUpdatePacket outPacket = new GameDescriptorReadyUpdatePacket();
		outPacket.id = session.getGameDescriptorId();
		outPacket.name = session.getAccount().getName();
		outPacket.ready = packet.ready;
		gameServer.getServer().sendToAllTCP(outPacket);

	}
}
