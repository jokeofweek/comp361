package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;

public class LobbyPanel extends ClientPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int COMPONENT_SPACING = 5;
	
	public LobbyPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		setSize(d);
		setPreferredSize(d);
		
		SwagFactory.style(this);
		
		// Add the logo at the top
		JLabel logoLabel = new JLabel(new ImageIcon(SwagFactory.SMALL_LOGO_IMAGE));
		Dimension logoSize = new Dimension(SwagFactory.SMALL_LOGO_IMAGE.getWidth(),
				SwagFactory.SMALL_LOGO_IMAGE.getHeight());
		logoLabel.setSize(logoSize);
		logoLabel.setMinimumSize(logoSize);
		logoLabel.setMaximumSize(logoSize);
		add(logoLabel, BorderLayout.NORTH);
		
		// Create a container for the chat, players panel, and button panel
		JPanel container = new JPanel(new BorderLayout());
		SwagFactory.style(container);
		
		container.add(new JLabel("Buttons go here"), BorderLayout.NORTH);
		container.add(new PlayersPanel(), BorderLayout.EAST);
		container.add(new ChatPanel(), BorderLayout.CENTER);
		add(container, BorderLayout.CENTER);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
