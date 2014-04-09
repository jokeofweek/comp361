package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.data.GameResult;
import comp361.shared.packets.shared.GameOverPacket;

public class GameOverPacketHandler implements ServerPacketHandler<GameOverPacket> {
	public void handle(Session session, GameServer gameServer, GameOverPacket packet) {
		String loser = null;
		
		// If the loser sent the packet, simply use the session that sent it
		int id = session.getGameDescriptorId();
		if (packet.result == GameResult.LOSS) {
			loser = session.getAccount().getName();
		// If the winner sent the packet, have to find the loser 
		} else {
			for (Connection c : gameServer.getServer().getConnections()) {
				Session s = (Session)c;
				if (s != session && id == s.getGameDescriptorId()) {
					loser = s.getAccount().getName();
				}
			}
		}
		
		gameServer.getGameDescriptorManager().endGame(id, gameServer, false, loser, packet.message, false, packet.serverForward);
		
	}
}
