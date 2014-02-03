package comp361.client.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.shared.Constants;

/**
 * This is the main client window.
 */
public class ClientWindow extends JFrame {

	private static final long serialVersionUID = -7223752548249858973L;

	private GameClient gameClient;
	private ClientPanel panel;

	public ClientWindow(GameClient client) {
		this.gameClient = client;

		
		Dimension dimensions = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		this.setSize(dimensions);
		this.setPreferredSize(dimensions);
		this.setMaximumSize(dimensions);
		this.setMinimumSize(dimensions);
		
		// Add a window listener for closing the socket.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Shutting down client.");
				// Shut down the client on window closing.
				gameClient.getClient().stop();
			}
		});
		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	public GameClient getGameClient() {
		return gameClient;
	}
	
	public void setPanel(final ClientPanel panel) {
		// Add the new panel as a listener
		final ClientWindow self = this;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remove the old panel as a listener
				if (self.panel != null) {
					gameClient.deleteObserver(panel);
				}
				
				self.panel = panel;
				self.getContentPane().removeAll();
				self.getContentPane().add(panel);
				self.revalidate();
				self.gameClient.addObserver(panel);	
			}
		});
	}

}
