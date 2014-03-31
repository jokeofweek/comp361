package comp361.client.network.handlers;

import javax.swing.JOptionPane;

import comp361.client.GameClient;
import comp361.shared.packets.shared.RequestSavePacket;

public class RequestSavePacketHandler implements ClientPacketHandler<RequestSavePacket> {

	@Override
	public void handle(GameClient gameClient, RequestSavePacket packet) {
		// Show an alert
		int result = JOptionPane.showConfirmDialog(null, packet.requester + 
				" has requested that the game be saved and postponed for a later date. Do you accept?",
				"Battleships",
				JOptionPane.YES_NO_OPTION);
		
		if (result == JOptionPane.YES_OPTION) {
			
		} else {
			
		}
		
	}

}
