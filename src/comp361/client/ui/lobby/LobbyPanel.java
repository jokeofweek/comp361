package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	
	private JPanel contentContainer;
	
	// Chat panel and container
	private JPanel chatContainer;
	private ChatPanel chatPanel;
	
	// Games panel 
	private JPanel gamesPanel;
	
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
		
		// Setup the  buttons
		JButton chatButton = new JButton("Chat");
		chatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContentPanel(chatContainer);
			}
		});
		
		JButton gamesButton = new JButton("Games");
		gamesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContentPanel(gamesPanel);
			}
		});
		
		JButton[] buttons = new JButton[] {
			chatButton,
			gamesButton,
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
				
		// Create the content container
		contentContainer = new JPanel(new BorderLayout());
	
		// Create the content panels
		setupChatContainer();
		setupGamesContainer();
		
		// Start at chat
		setContentPanel(chatContainer);
		
		// Add the content container
		container.add(contentContainer);
		add(container, BorderLayout.CENTER);
		
		// Create the invite overlay panel
		inviteOverlayPanel = new InviteOverlayPanel();
		inviteOverlayPanel.setBounds(Constants.SCREEN_WIDTH - inviteOverlayPanel.getWidth() - OVERLAY_PADDING_RIGHT, 
			0, inviteOverlayPanel.getWidth(), inviteOverlayPanel.getHeight());
	}
	
	private void setupChatContainer() {
		chatContainer = new JPanel(new BorderLayout());
		
		playersPanel = new PlayersPanel(getGameClient());
		chatContainer.add(playersPanel, BorderLayout.EAST);
		
		chatPanel = new ChatPanel(getGameClient());
		chatContainer.add(chatPanel, BorderLayout.CENTER);
	}
	
	private void setupGamesContainer() {
		gamesPanel = new JPanel(new BorderLayout());
		gamesPanel.add(new JLabel("ABCDEF"));
	}
	
	/**
	 * Updates the content panel to a new panel
	 * (eg. games view or chat view)
	 * @param panel The panel to use as content.
	 */
	private void setContentPanel(final JPanel panel) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentContainer.removeAll();
				contentContainer.add(panel, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
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
