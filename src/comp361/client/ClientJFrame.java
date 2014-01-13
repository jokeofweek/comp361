package comp361.client;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;

import javax.swing.JFrame;

/**
 * All application frames should extend this class to ensure we properly close
 * sockets and allow handling messages received from the client.
 */
public abstract class ClientJFrame extends JFrame implements Observer {

	private static final long serialVersionUID = -7223752548249858973L;

	private GameClient gameClient;

	public ClientJFrame(GameClient client) {
		this.gameClient = client;

		// Add a window listener for closing the socket.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Shutting down client.");
				// Shut down the client on window closing.
				gameClient.getClient().stop();
			}
		});
		
		// Add listener for adding and removing this frame as a listener of
		// the game client.
		final ClientJFrame self = this;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				System.out.println("Adding game observer -> " + self);
				gameClient.addObserver(self);
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				System.out.println("Removing game observer -> " + self);
				gameClient.deleteObserver(self);
			}
		});
	}

	public GameClient getGameClient() {
		return gameClient;
	}

}
