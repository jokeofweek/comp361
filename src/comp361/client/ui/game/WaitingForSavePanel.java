package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;

public class WaitingForSavePanel extends ClientPanel {

	private ClientPanel panel;
	
	public WaitingForSavePanel(GameClient client, final ClientWindow window, final ClientPanel panel) {
		super(client, window, new BorderLayout());
		this.panel = panel;
		JLabel label = new JLabel("Waiting for response...");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
		
		new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.setPanel(panel);
			}
		}).start();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {

	}

}
