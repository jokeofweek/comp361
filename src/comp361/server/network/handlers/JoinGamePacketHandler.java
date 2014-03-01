package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.data.GameDescriptorManager;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GenericError;

public class JoinGamePacketHandler implements
		ServerPacketHandler<JoinGamePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			JoinGamePacket object) {
		GameDescriptorManager manager = gameServer.getGameDescriptorManager();

		// Try to join the game, errorring out if it is full
		if (!manager.addPlayer(object.id, session.getAccount().getName())) {
			session.sendTCP(GenericError.GAME_IS_FULL);
			return;
		}

		// There was space, so join the game
		session.setSessionType(SessionType.GAME_SETUP);
		session.setGameDescriptorId(object.id);
		
		// Create the update packet
		GameDescriptorPlayerUpdatePacket packet = new GameDescriptorPlayerUpdatePacket();
		packet.id = object.id;
		packet.name = session.getAccount().getName();
		packet.joined = true;
		
		gameServer.getServer().sendToAllTCP(packet);

	}
}
