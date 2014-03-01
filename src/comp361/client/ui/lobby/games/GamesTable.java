package comp361.client.ui.lobby.games;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.sun.corba.se.impl.logging.OMGSystemException;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.util.ButtonColumn;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.JoinGamePacket;

public class GamesTable extends JTable {

	private GamesTableModel model;
	private static final int ROW_HEIGHT = SwagFactory.BUTTON_HEIGHT;
	
	public GamesTable(final GameClient client, final GamesTableModel model) {
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
		        GameDescriptor descriptor = model.getGameDescriptor(modelRow);
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
		        
			}
		}, GamesTableModel.JOIN_COLUMN);
	}
	
}
