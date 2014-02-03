package comp361.client.ui;

import java.awt.LayoutManager;
import java.util.Observer;

import javax.swing.JPanel;

import comp361.client.GameClient;

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
		this.gameClient = gameClient;
		this.clientWindow = clientWindow;
	}
	
	public ClientPanel(GameClient gameClient, ClientWindow clientWindow, LayoutManager layoutManager) {
		super(layoutManager);
		this.gameClient = gameClient;
		this.clientWindow = clientWindow;
	}
	
	public GameClient getGameClient() {
		return gameClient;
	}
	
	public ClientWindow getClientWindow() {
		return clientWindow;
	}
	
}
