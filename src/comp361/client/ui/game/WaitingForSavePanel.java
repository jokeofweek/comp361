package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Savepoint;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.packets.shared.SaveResponsePacket;

public class WaitingForSavePanel extends ClientPanel {

	private ClientPanel panel;
	
	public WaitingForSavePanel(GameClient client, final ClientWindow window, final ClientPanel panel) {
		super(client, window, new BorderLayout());
		this.panel = panel;
		JLabel label = new JLabel("Waiting for response...");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// Switch either to lobby or back to game if the save was accepted
		if (arg instanceof SaveResponsePacket) {
			System.out.println("Save response: " + ((SaveResponsePacket)arg).accepted);
			if (((SaveResponsePacket)arg).accepted) {
				getClientWindow().setPanel(new LobbyPanel(getGameClient(), getClientWindow()));	
			} else {
				getClientWindow().setPanel(panel);
			}
		}
	}

}
