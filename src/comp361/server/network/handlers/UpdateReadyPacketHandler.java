package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.Game;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;
import comp361.shared.packets.server.GameDescriptorStartPacket;
import comp361.shared.packets.server.GameStartPacket;

public class UpdateReadyPacketHandler implements
		ServerPacketHandler<UpdateReadyPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			UpdateReadyPacket packet) {
		// Update the game internally.
		gameServer.getGameDescriptorManager().setReadyStatus(
				session.getGameDescriptorId(), session.getAccount().getName(),
				packet.ready);
		gameServer.getGameDescriptorManager().setPlayerPositions(
				session.getGameDescriptorId(), session.getAccount().getName(),
				packet.positions);

		if (gameServer.getGameDescriptorManager().canStart(
				session.getGameDescriptorId())) {
			// Send a start packet to everyone to let them know the game started
			GameDescriptorStartPacket gdStartPacket = new GameDescriptorStartPacket();
			gdStartPacket.id = session.getGameDescriptorId();
			gameServer.getServer().sendToAllTCP(gdStartPacket);
			gameServer.getLogger().debug("Game " + session.getGameDescriptorId() + " started");
			// Create the game
			Game game = gameServer.getGameDescriptorManager().createGame(
					session.getGameDescriptorId());
			// Send the game to the two players
			GameStartPacket startPacket = new GameStartPacket();
			startPacket.game = game;
			// Send to all connections that are in the game
			for (Connection connection : gameServer.getServer().getConnections()) {
				Session s = (Session) connection;
				if (s.getSessionType() == SessionType.GAME_SETUP && s.getGameDescriptorId() == session.getGameDescriptorId()) {
					gameServer.getServer().sendToTCP(s.getID(), startPacket);
				}
				s.setSessionType(SessionType.GAME);
			}
			
		} else {
			// Notify the players
			GameDescriptorReadyUpdatePacket outPacket = new GameDescriptorReadyUpdatePacket();
			outPacket.id = session.getGameDescriptorId();
			outPacket.name = session.getAccount().getName();
			outPacket.ready = packet.ready;
			gameServer.getServer().sendToAllTCP(outPacket);
			
			if (packet.ready) {
				gameServer.getLogger().debug("Player " + session.getAccount().getName() + " ready in game " + session.getGameDescriptorId());
			} else {
				gameServer.getLogger().debug("Player " + session.getAccount().getName() + " not ready in game " + session.getGameDescriptorId());
			}
		}

	}
}
