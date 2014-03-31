package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.packets.client.RequestSavedGamesPacket;
import comp361.shared.packets.server.SavedGamesListPacket;

public class RequestSavedGamesPacketHandler implements
		ServerPacketHandler<RequestSavedGamesPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			RequestSavedGamesPacket packet) {
		SavedGamesListPacket response = new SavedGamesListPacket();
		response.containers = gameServer.getSaveGameManager().getSavedGames(
				session.getAccount().getName(), false);
		session.sendTCP(response);
	}
}
