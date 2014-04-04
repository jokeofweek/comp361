package comp361.client.network.handlers;

import javax.swing.JOptionPane;

import comp361.client.GameClient;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.shared.SavedGameInvitePacket;

public class SavedGameInvitePacketHandler implements
		ClientPacketHandler<SavedGameInvitePacket> {
	public void handle(GameClient gameClient, SavedGameInvitePacket packet) {
		// Prompt the user!
		GameDescriptor desc = packet.container.descriptor;
		String otherPlayer = desc.getPlayers()[0].equals(gameClient.getPlayerName()) ?
				desc.getPlayers()[1] : desc.getPlayers()[0];
				
		int response = JOptionPane.showConfirmDialog(null, otherPlayer + " has invited you to continue the game '" + 
				desc.getName() + "'. Do you accept?", "Battleships", JOptionPane.YES_NO_OPTION);	
				
	}
}
