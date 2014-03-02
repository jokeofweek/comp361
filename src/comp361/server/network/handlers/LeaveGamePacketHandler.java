package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;

public class LeaveGamePacketHandler implements
		ServerPacketHandler<LeaveGamePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			LeaveGamePacket packet) {
		int descriptorId = session.getGameDescriptorId();
		// If the game hasn't started, it's simple
		if (!gameServer.getGameDescriptorManager().gameHasStarted(descriptorId)) {
			gameServer.getGameDescriptorManager().removePlayer(descriptorId, session.getAccount().getName());
			
			// Set the session back to lobby
			session.setSessionType(SessionType.LOBBY);
			session.setGameDescriptorId(0);
			
			// Create the update packet
			GameDescriptorPlayerUpdatePacket updatePacket = new GameDescriptorPlayerUpdatePacket();
			updatePacket.id = descriptorId;
			updatePacket.name = session.getAccount().getName();
			updatePacket.joined = false;
			
			gameServer.getServer().sendToAllTCP(updatePacket);
		} else {
			// TODO Handle game already started
		}

	}
}
