package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.packets.shared.MessagePacket;

public class LobbyPanel extends ClientPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int COMPONENT_SPACING = 5;
	public static final int OVERLAY_PADDING_RIGHT = 10;
	
	private ChatPanel chatPanel;
	private PlayersPanel playersPanel;
	private InviteOverlayPanel inviteOverlayPanel;
	
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
		
		JButton[] buttons = new JButton[] {
			new JButton("Chat"),
			new JButton("Games"),
			new JButton("Statistics"),
			new JButton("New Game"),
			new JButton("Load Game")
		};
		
		JPanel buttonContainer = new JPanel(new GridLayout(1, 5, COMPONENT_SPACING, 0));
		buttonContainer.setBorder(BorderFactory.createEmptyBorder(0, COMPONENT_SPACING, 0, COMPONENT_SPACING));
		SwagFactory.style(buttonContainer);

		for (JButton button : buttons) {
			// Set up the button style.
			SwagFactory.style(button);
			
			d = new Dimension(button.getWidth(), SwagFactory.BUTTON_HEIGHT);
			button.setMaximumSize(d);
			button.setMinimumSize(d);
			button.setPreferredSize(d);
			button.setSize(d);
			
			// Add it to the container
			buttonContainer.add(button);
		}
		
		container.add(buttonContainer, BorderLayout.NORTH);
		
		playersPanel = new PlayersPanel(getGameClient());
		container.add(playersPanel, BorderLayout.EAST);
		
		chatPanel = new ChatPanel(getGameClient());
		container.add(chatPanel, BorderLayout.CENTER);
		add(container, BorderLayout.CENTER);
		
		// Create the invite overlay panel
		inviteOverlayPanel = new InviteOverlayPanel();
		inviteOverlayPanel.setBounds(Constants.SCREEN_WIDTH - inviteOverlayPanel.getWidth() - OVERLAY_PADDING_RIGHT, 
			0, inviteOverlayPanel.getWidth(), inviteOverlayPanel.getHeight());
	}
	
	@Override
	public void enter() {
		chatPanel.getMessageField().requestFocusInWindow();
		// On enter, register the players table model as an observer
		// of the player manager so that it can update.
		getGameClient().getPlayerManager().addObserver(playersPanel.getTableModel());
	}
	
	@Override
	public void exit() {
		// When we leave this screen, we don't want to receive updates
		// from the player manager anymore.
		getGameClient().getPlayerManager().deleteObserver(playersPanel.getTableModel());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Received: " + arg);
		if (arg instanceof MessagePacket) {
			chatPanel.publishChatMessage((MessagePacket) arg);
		}		
	}
	
	@Override
	public JComponent getOverlayComponent() {
		return inviteOverlayPanel;
		//return null;
	}
}
