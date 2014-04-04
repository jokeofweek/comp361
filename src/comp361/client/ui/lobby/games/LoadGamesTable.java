package comp361.client.ui.lobby.games;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.util.ButtonColumn;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.server.SavedGameContainer;

public class LoadGamesTable extends JTable {

	private LoadGamesTableModel model;
	private static final int ROW_HEIGHT = SwagFactory.BUTTON_HEIGHT;

	public LoadGamesTable(final GameClient client, final LoadGamesTableModel model) {
		super(null, model.getHeaders());
		setModel(model);

		// Set the table styling
		setRowHeight(ROW_HEIGHT);

		// Create the button column
		new ButtonColumn(this, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );

		        // Get the game descriptor
		        SavedGameContainer container = model.getContainer(modelRow);
		        
		        // Ensure the player from the saved game is online and isn't already in a game
		        String otherPlayer = container.descriptor.getPlayers()[0].equals(client.getPlayerName()) ?
		        		container.descriptor.getPlayers()[1] : container.descriptor.getPlayers()[0];
		        
		        if (client.getPlayerManager().getPlayer(otherPlayer) == null) {
		        	JOptionPane.showMessageDialog(null, otherPlayer + " is not online right now!");
		        	return;
		        }

		        // Iterate through all game descriptors, making sure the player isn't already in a game
		        for (GameDescriptor descriptor : client.getGameDescriptorManager().getGameDescriptors()) {
		        	for (String player : descriptor.getPlayers()) {
		        		if (player != null && otherPlayer.equals(player)) {
		        			JOptionPane.showMessageDialog(null, otherPlayer + " is already in a game!");
				        	return;
		        		}
		        	}
		        }
		        
		        /*GameDescriptor descriptor = model.getGameDescriptor(modelRow);
		        if (!descriptor.isStarted()) {
		        	boolean canJoin = false;
		        	// Prompt for password if there is a game password
		        	if (descriptor.getPassword() != null && !descriptor.getPassword().isEmpty()) {
		        		String input = JOptionPane.showInputDialog(null, "Please enter the game password:");
		        		if (input.equals(descriptor.getPassword())) {
		        			canJoin = true;
		        		} else {
		        			JOptionPane.showMessageDialog(null, "You entered an incorrect password.");
		        		}
		        	} else {
		        		canJoin = true;
		        	}

		        	// Send the packet if we got the password right.
		        	if (canJoin) {
		        		JoinGamePacket packet = new JoinGamePacket();
		        		packet.id = descriptor.getId();
		        		client.getClient().sendTCP(packet);
		        	}
		        } else {
		        	// TODO Handle case where you want to observe a game.
		        }
*/
			}
		}, LoadGamesTableModel.JOIN_COLUMN);
	}

}
