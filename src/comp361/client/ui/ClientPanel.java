package comp361.client.ui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observer;

import javax.swing.JComponent;
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
	
	/**
	 * This callback can be overridden by ClientPanels and is executed
	 * when the player enters the screen.
	 */
	public void enter() {}
	
	/**
	 * This callback can be overridden by ClientPanels and is executed
	 * when the player changes to another screen.
	 */
	public void exit(){}
	
	/**
	 * This allows a client panel to specify a component that is rendered over
	 * this panel (eg. pop ups which appear over the screen).
	 * @return a component which will appear over the panel (or null)
	 */
	public JComponent getOverlayComponent() {
		return null;
	}
}
