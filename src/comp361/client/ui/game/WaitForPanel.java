package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Savepoint;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.packets.shared.SaveResponsePacket;

public class WaitForPanel extends ClientPanel {

	private ClientPanel panel;
	private Callback action;
	
	public WaitForPanel(GameClient client, final ClientWindow window, final ClientPanel panel, final Callback action) {
		super(client, window, new BorderLayout());
		this.panel = panel;
		this.action = action;
		
		JLabel label = new JLabel("Waiting for response...");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
	}
	
	public static interface Callback {
		/**
		 * @param object the packet received.
		 * @return true if we should switch back to old panel.
		 */
		public boolean receivePacket(Object object);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		if (this.action.receivePacket(arg)) {
			getClientWindow().setPanel(panel);
		}
	}

}
