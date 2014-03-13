package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.shared.InGameMessagePacket;
import comp361.shared.packets.shared.SetupMessagePacket;

public class InGameMessagePacketHandler implements ServerPacketHandler<InGameMessagePacket>
{
	@Override
	public void handle(Session session, GameServer gameServer,
			InGameMessagePacket packet) {

		int id = session.getGameDescriptorId();
		
		for(Connection connection : gameServer.getServer().getConnections())
		{			
			Session s = (Session) connection;
			
			if(s.getSessionType() == SessionType.GAME && s != session)
			{
				if(id == s.getGameDescriptorId())
				{
					gameServer.getServer().sendToTCP(s.getID(), packet);
					gameServer.getLogger().debug("Player " + packet.senderName + " sent message \"" + packet.message + "\" in game " + id);
				}
			}
		}
	}
}
