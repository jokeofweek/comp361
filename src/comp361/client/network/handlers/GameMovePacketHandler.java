package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.shared.GameMovePacket;

public class GameMovePacketHandler implements
		ClientPacketHandler<GameMovePacket> {
	@Override
	public void handle(GameClient gameClient, GameMovePacket packet) {
		gameClient.getGameManager().applyMove(packet, false);
	}
}
