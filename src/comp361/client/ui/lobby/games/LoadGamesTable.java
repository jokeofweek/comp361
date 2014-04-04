package comp361.client.ui.lobby.games;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.game.WaitForPanel;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.client.ui.util.ButtonColumn;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.server.SavedGameContainer;
import comp361.shared.packets.shared.SavedGameInvitePacket;

public class LoadGamesTable extends JTable {

	private LoadGamesTableModel model;
	private static final int ROW_HEIGHT = SwagFactory.BUTTON_HEIGHT;

	public LoadGamesTable(final GameClient client, final ClientWindow window, final LoadGamesTableModel model) {
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
		        
		        // Get the current panel
		        ClientPanel panel = window.getPanel();
		        window.setPanel(new WaitForPanel(client, window, panel, new WaitForPanel.Callback() {
					
					@Override
					public boolean receivePacket(Object object) {
						// TODO Auto-generated method stub
						return false;
					}
				}));
		        // Send the packet
		        SavedGameInvitePacket packet = new SavedGameInvitePacket();
		        packet.container = container;
		        client.getClient().sendTCP(packet);
			}
		}, LoadGamesTableModel.JOIN_COLUMN);
	}

}
