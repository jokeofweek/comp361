package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.shared.RequestSavePacket;
import comp361.shared.packets.shared.SetupMessagePacket;

public class RequestSavePacketHandler implements
		ServerPacketHandler<RequestSavePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			RequestSavePacket packet) {
		int id = session.getGameDescriptorId();
		for (Connection connection : gameServer.getServer().getConnections()) {
			Session s = (Session) connection;
			if (s.getSessionType() == SessionType.GAME) {
				if (id == s.getGameDescriptorId() && s != session) {
					gameServer.getServer().sendToTCP(s.getID(), packet);
				}
			}
		}
	}
}
