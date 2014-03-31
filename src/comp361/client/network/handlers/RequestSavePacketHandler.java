package comp361.client.network.handlers;

import javax.swing.JOptionPane;

import comp361.client.GameClient;
import comp361.shared.packets.shared.RequestSavePacket;
import comp361.shared.packets.shared.SaveResponsePacket;

public class RequestSavePacketHandler implements ClientPacketHandler<RequestSavePacket> {

	@Override
	public void handle(GameClient gameClient, RequestSavePacket packet) {
		// Show an alert
		int result = JOptionPane.showConfirmDialog(null, packet.requester + 
				" has requested that the game be saved and postponed for a later date. Do you accept?",
				"Battleships",
				JOptionPane.YES_NO_OPTION);
		
		SaveResponsePacket response = new SaveResponsePacket();
		if (result == JOptionPane.YES_OPTION) {
			response.accepted = true;
			response.game = gameClient.getGameManager().getGame();
			
			// Publish message
			gameClient.publishMessage(response);
		} else {
			response.accepted = false;
		}

		
		gameClient.getClient().sendTCP(response);
		
	}

}
