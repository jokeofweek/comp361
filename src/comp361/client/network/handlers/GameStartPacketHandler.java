package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.data.Ship;
import comp361.shared.packets.server.GameStartPacket;

public class GameStartPacketHandler implements
		ClientPacketHandler<GameStartPacket> {
	@Override
	public void handle(GameClient gameClient, GameStartPacket packet) {
		// Dirty hack, but prevents circular references
		for (Ship s : packet.game.getShips()) {
			s.setGame(packet.game);
		}
		gameClient.getGameManager().setGame(packet.game);
		gameClient.publishMessage(packet);
	}
}
