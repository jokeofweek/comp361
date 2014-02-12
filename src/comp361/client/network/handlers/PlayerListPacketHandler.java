package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.PlayerListPacket;

public class PlayerListPacketHandler implements ClientPacketHandler<PlayerListPacket> {

	@Override
	public void handle(GameClient gameClient, PlayerListPacket object) {
		// Pass the packet along to the player manager	
		gameClient.getPlayerManager().updatePlayerList(object);
		// Publish to the game client
		gameClient.publishMessage(object);
	}

}
