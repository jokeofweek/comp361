package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.shared.MessagePacket;

public class PlayerUpdatePacketHandler implements ClientPacketHandler<PlayerUpdatePacket> {

	@Override
	public void handle(GameClient gameClient, PlayerUpdatePacket object) {
		// Check if player is logging in, out, or updating
		MessagePacket message;
		switch (object.status) {
		case LOGGED_IN:
			gameClient.getPlayerManager().addPlayer(object.player);
			message = new MessagePacket();
			message.message = object.player.getName() + " has connected.";
			gameClient.publishMessage(message);
			break;
		case LOGGED_OUT:
			gameClient.getPlayerManager().removePlayer(object.player.getName());
			message = new MessagePacket();
			message.message = object.player.getName() + " has disconnected.";
			gameClient.publishMessage(message);
			break;
		case UPDATE:
			throw new RuntimeException("Not implemented yet.");
		}
	}

}
