package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.shared.MessagePacket;

public class PlayerUpdatePacketHandler implements ClientPacketHandler<PlayerUpdatePacket> {

	@Override
	public void handle(GameClient gameClient, PlayerUpdatePacket object) {
		String message = "";

		// Check if player is logging in, out, or updating
		switch (object.status) {
		case LOGGED_IN:
			gameClient.getPlayerManager().addPlayer(object.player);
			message = object.player.getName() + " has connected.";
			break;
		case LOGGED_OUT:
			gameClient.getPlayerManager().removePlayer(object.player.getName());
			message = object.player.getName() + " has disconnected.";
			break;
		case UPDATE:
			throw new RuntimeException("Not implemented yet.");
		}

		MessagePacket messagePacket = new MessagePacket();
		messagePacket.message = message;
		messagePacket.isMetaMessage = true;
		gameClient.publishMessage(messagePacket);
	}

}
