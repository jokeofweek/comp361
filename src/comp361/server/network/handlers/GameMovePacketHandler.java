package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.shared.GameMovePacket;

public class GameMovePacketHandler implements
		ServerPacketHandler<GameMovePacket> {

	@Override
	public void handle(Session session, GameServer gameServer,
			GameMovePacket packet) {

		// Send to all players that are in the game
		int id = session.getGameDescriptorId();
		
		gameServer.getLogger().debug("Received move for game " + id);
		
		for (Connection c : gameServer.getServer().getConnections()) {
			Session other = (Session) c;
			if (other != session && other.getSessionType() == SessionType.GAME
					&& other.getGameDescriptorId() == id) {
				gameServer.getServer().sendToTCP(other.getID(), packet);
			}
		}
	}

}
