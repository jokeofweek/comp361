package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.shared.GameOverPacket;

public class GameOverPacketHandler implements ClientPacketHandler<GameOverPacket> {
	public void handle(GameClient gameClient, GameOverPacket packet) {
		gameClient.getGameManager().gameOver(packet);
	}
}