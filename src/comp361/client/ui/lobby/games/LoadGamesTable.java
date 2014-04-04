package comp361.client.ui.lobby.games;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.game.GamePanel;
import comp361.client.ui.game.WaitForPanel;
import comp361.client.ui.util.ButtonColumn;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.server.SavedGameContainer;
import comp361.shared.packets.shared.SavedGameInvitePacket;
import comp361.shared.packets.shared.SavedGameInviteResponsePacket;

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
		        final SavedGameContainer container = model.getContainer(modelRow);
		        
		        // Ensure the player from the saved game is online and isn't already in a game
		        final String otherPlayer = container.descriptor.getPlayers()[0].equals(client.getPlayerName()) ?
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
		        window.setPanel(new WaitForPanel(client, window, panel) {
					
		        	@Override
		        	public void enter() {
		        		// Send the invitation packet
				        SavedGameInvitePacket packet = new SavedGameInvitePacket();
				        packet.container = container;
				        client.getClient().sendTCP(packet);
		        	}
		        	
					@Override
					public boolean receivePacket(Object object) {
						if (object instanceof SavedGameInviteResponsePacket) {
							if (!((SavedGameInviteResponsePacket)object).accepted) {
								JOptionPane.showMessageDialog(null, otherPlayer + " denied your invitation.");
								return true;
							}
						} else if (object instanceof GameStartPacket) {
							// Switch screens
							window.setPanel(new GamePanel(client, window));
						}
						return false;
					}
				});
			}
		}, LoadGamesTableModel.JOIN_COLUMN);
	}

}
