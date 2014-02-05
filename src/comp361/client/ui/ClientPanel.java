package comp361.client.ui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observer;

import javax.swing.JPanel;

import comp361.client.GameClient;
import comp361.shared.Constants;

/**
 * All panels which will appear in the client window should extend this class.
 * It instantly sets up the connection between the panel and the {@link GameClient} in
 * order to receive messages.
 */
public abstract class ClientPanel extends JPanel implements Observer {

	private GameClient gameClient;
	private ClientWindow clientWindow;
	
	public ClientPanel(GameClient gameClient, ClientWindow clientWindow) {
		super();
		setup(gameClient, clientWindow);
	}
	
	public ClientPanel(GameClient gameClient, ClientWindow clientWindow, LayoutManager layoutManager) {
		super(layoutManager);
		setup(gameClient, clientWindow);
	}

	private void setup(GameClient gameClient, ClientWindow clientWindow) {
		this.gameClient = gameClient;
		this.clientWindow = clientWindow;

		SwagFactory.style(this);
		
		Dimension dimensions = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		this.setSize(dimensions);
		this.setMaximumSize(dimensions);
		this.setMinimumSize(dimensions);
		this.setPreferredSize(dimensions);
		
		
	}
	
	public GameClient getGameClient() {
		return gameClient;
	}
	
	public ClientWindow getClientWindow() {
		return clientWindow;
	}
	
}
