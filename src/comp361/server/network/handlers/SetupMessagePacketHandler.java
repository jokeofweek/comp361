package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.shared.SetupMessagePacket;

public class SetupMessagePacketHandler implements ServerPacketHandler<SetupMessagePacket>
{
	@Override
	public void handle(Session session, GameServer gameServer,
			SetupMessagePacket packet) {

		int id = session.getGameDescriptorId();
		
		for(Connection connection : gameServer.getServer().getConnections())
		{			
			Session s = (Session) connection;
			
			if(s.getSessionType() == SessionType.GAME_SETUP)
			{
				if(id == s.getGameDescriptorId())
				{
					gameServer.getServer().sendToTCP(s.getID(), packet);
				}
			}
		}
	}
}
