package comp361.client.ui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
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
	private JLayeredPane layeredPane;

	public ClientWindow(GameClient client) {
		this.gameClient = client;
		
		Dimension dimensions = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		this.getContentPane().setSize(dimensions);
		this.getContentPane().setPreferredSize(dimensions);
		this.getContentPane().setMaximumSize(dimensions);
		this.getContentPane().setMinimumSize(dimensions);
		
		// Setup the layered pane
		this.layeredPane = new JLayeredPane();
		this.layeredPane.setSize(dimensions);
		this.layeredPane.setPreferredSize(dimensions);
		this.layeredPane.setMaximumSize(dimensions);
		this.layeredPane.setMinimumSize(dimensions);
		this.add(layeredPane);
		
		// Add a window listener for closing the socket.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Shutting down client.");
				
				gameClient.getClient().stop();
			}
		});
		

		setPanel(new LoadingPanel(client, this));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Battleships");
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public GameClient getGameClient() {
		return gameClient;
	}
	
	public ClientPanel getPanel() {
		return panel;
	}
	
	public void setPanel(final ClientPanel panel) {
		// Add the new panel as a listener
		final ClientWindow self = this;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Remove the old panel as a listener
				if (self.panel != null) {
					gameClient.deleteObserver(self.panel);
					self.panel.exit();
				}
				
				self.panel = panel;

				layeredPane.removeAll();
				layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
				
				// Setup the overlay component
				JComponent overlayComponent = panel.getOverlayComponent();
				if (overlayComponent != null) {
					layeredPane.add(overlayComponent, JLayeredPane.PALETTE_LAYER);
				}

				self.revalidate();
				self.gameClient.addObserver(panel);	
				self.panel.enter();

			}
		});
	}

}
