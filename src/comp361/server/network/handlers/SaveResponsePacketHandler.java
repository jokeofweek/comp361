package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.Game;
import comp361.shared.packets.shared.SaveResponsePacket;

public class SaveResponsePacketHandler implements
		ServerPacketHandler<SaveResponsePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			SaveResponsePacket packet) {
		Game game = packet.game;
		
		
		// Forward the response to the other player.
		// Note that the packet's game is null from now on since we don't need to
		// send the game to the other player.
		packet.game = null; 
		
		int id = session.getGameDescriptorId();
		for (Connection connection : gameServer.getServer().getConnections()) {
			Session s = (Session) connection;
			if (s.getSessionType() == SessionType.GAME) {
				if (id == s.getGameDescriptorId() && s != session) {
					gameServer.getServer().sendToTCP(s.getID(), packet);
					if (packet.accepted) {
						s.setSessionType(SessionType.LOBBY);
						s.setGameDescriptorId(-1);
					}
				}
			}
		}
		
		// If the save was accepted, set state back to lobby
		if (packet.accepted) {
			// Actually save the game
			gameServer.getSaveGameManager().saveGame(game, id);
			
			// Remove the game descriptor
			gameServer.getGameDescriptorManager().removeGameDescriptor(id, gameServer);

			session.setSessionType(SessionType.LOBBY);
			session.setGameDescriptorId(-1);
		}
	}
}
