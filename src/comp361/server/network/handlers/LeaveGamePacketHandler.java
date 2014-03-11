package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.shared.GameOverPacket;

public class LeaveGamePacketHandler implements
		ServerPacketHandler<LeaveGamePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			LeaveGamePacket packet) {
		int descriptorId = session.getGameDescriptorId();
		// If the game hasn't started, it's simple
		if (!gameServer.getGameDescriptorManager().hasStarted(descriptorId)) {
			gameServer.getGameDescriptorManager().removePlayer(descriptorId,
					session.getAccount().getName());

			// Set the session back to lobby
			session.setSessionType(SessionType.LOBBY);
			session.setGameDescriptorId(0);

			// Create the update packet
			GameDescriptorPlayerUpdatePacket updatePacket = new GameDescriptorPlayerUpdatePacket();
			updatePacket.id = descriptorId;
			updatePacket.name = session.getAccount().getName();
			updatePacket.joined = false;

			gameServer.getServer().sendToAllTCP(updatePacket);
			gameServer.getLogger().debug(
					"Player " + session.getAccount().getName() + " left game "
							+ descriptorId);
		} else {
			gameServer.getGameDescriptorManager().endGame(
					session.getGameDescriptorId(), gameServer, false,
					session.getAccount().getName(),
					session.getAccount().getName() + " left the game.", false);
		}
	}
}
