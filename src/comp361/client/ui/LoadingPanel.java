package comp361.client.ui;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comp361.client.GameClient;

public class LoadingPanel extends ClientPanel {

	public LoadingPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		JLabel label = new JLabel("Loading...", SwingConstants.CENTER);
		add(label, BorderLayout.CENTER);
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	
}
