package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.packets.shared.GameOverPacket;

public class GameOverPacketHandler implements ServerPacketHandler<GameOverPacket> {
	public void handle(Session session, GameServer gameServer, GameOverPacket packet) {
		int id = session.getGameDescriptorId();
		for (Connection c : gameServer.getServer().getConnections()) {
			Session s = (Session)c;
			if (s != session && id == s.getGameDescriptorId()) {
				gameServer.getServer().sendToAllExceptTCP(session.getID(), packet);
			}
		}
		gameServer.getLogger().debug("Game " + id + " is over");
	}
}
