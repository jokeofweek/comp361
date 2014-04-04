
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.game.GamePanel;
import comp361.client.ui.game.WaitForPanel;
import comp361.client.ui.lobby.chat.ChatPanel;
import comp361.client.ui.lobby.chat.PlayersPanel;
import comp361.client.ui.lobby.games.GamesPanel;
import comp361.client.ui.lobby.games.LoadGamesPanel;
import comp361.client.ui.setup.NewGamePanel;
import comp361.shared.Constants;
import comp361.shared.packets.client.RequestSavedGamesPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.server.GenericError;
import comp361.shared.packets.server.SavedGamesListPacket;
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

	// Games & load games panel
	private GamesPanel gamesPanel;
	private LoadGamesPanel loadGamesPanel;

	private PlayersPanel playersPanel;
	private InviteOverlayPanel inviteOverlayPanel;

	public LobbyPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());

		Dimension d = new Dimension(WIDTH, HEIGHT);
		setSize(d);
		setPreferredSize(d);

		SwagFactory.style(this);

		// Add the logo at the top
		JLabel logoLabel = new JLabel(new ImageIcon(
				SwagFactory.SMALL_LOGO_IMAGE));
		Dimension logoSize = new Dimension(
				SwagFactory.SMALL_LOGO_IMAGE.getWidth(),
				SwagFactory.SMALL_LOGO_IMAGE.getHeight());
		logoLabel.setSize(logoSize);
		logoLabel.setMinimumSize(logoSize);
		logoLabel.setMaximumSize(logoSize);
		add(logoLabel, BorderLayout.NORTH);

		// Create a container for the chat, players panel, and button panel
		JPanel container = new JPanel(new BorderLayout());
		SwagFactory.style(container);

		// Setup the buttons
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

		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new NewGameActionListener());

		JButton loadGameButton = new JButton("Load Game");
		final ClientPanel self = this;
		loadGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getClientWindow().setPanel(new WaitForPanel(getGameClient(), getClientWindow(), self) {
					@Override
					public void enter() {
						// Request the saved games
						getGameClient().getClient().sendTCP(new RequestSavedGamesPacket());
					}
					
					@Override
					public boolean receivePacket(Object object) {
						if (object instanceof SavedGamesListPacket) {
							loadGamesPanel.getTableModel().refreshData(((SavedGamesListPacket)object).containers);
							setContentPanel(loadGamesPanel);
							return true;
						}
						return false;
					}
				});
			}
		});

		JButton[] buttons = new JButton[] { chatButton, gamesButton,
				new JButton("Statistics"), newGameButton,
				loadGameButton };

		JPanel buttonContainer = new JPanel(new GridLayout(1, 5,
				COMPONENT_SPACING, 0));
		buttonContainer.setBorder(BorderFactory.createEmptyBorder(0,
				COMPONENT_SPACING, 0, COMPONENT_SPACING));
		SwagFactory.style(buttonContainer);

		for (JButton button : buttons) {
			// Set up the button style.
			SwagFactory.style(button);

			// Add it to the container
			buttonContainer.add(button);
		}

		container.add(buttonContainer, BorderLayout.NORTH);

		// Create the content container
		contentContainer = new JPanel(new BorderLayout());
		contentContainer.setBorder(BorderFactory.createEmptyBorder(
				COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING,
				COMPONENT_SPACING));
		SwagFactory.style(contentContainer);

		// Create the content panels
		setupChatContainer();
		setupGamesContainer();
		setupLoadGameContainer();

		// Start at chat
		setContentPanel(chatContainer);

		// Add the content container
		container.add(contentContainer);
		add(container, BorderLayout.CENTER);

		// Create the invite overlay panel
		inviteOverlayPanel = new InviteOverlayPanel();
		inviteOverlayPanel.setBounds(Constants.SCREEN_WIDTH
				- inviteOverlayPanel.getWidth() - OVERLAY_PADDING_RIGHT, 0,
				inviteOverlayPanel.getWidth(), inviteOverlayPanel.getHeight());
	}

	private void setupChatContainer() {
		chatContainer = new JPanel(new BorderLayout());

		playersPanel = new PlayersPanel(getGameClient());
		chatContainer.add(playersPanel, BorderLayout.EAST);

		chatPanel = new ChatPanel(getGameClient());
		chatContainer.add(chatPanel, BorderLayout.CENTER);
	}

	private void setupGamesContainer() {
		gamesPanel = new GamesPanel(getGameClient());
	}

	private void setupLoadGameContainer() {
		loadGamesPanel = new LoadGamesPanel(getGameClient(), getClientWindow());
	}

	/**
	 * Updates the content panel to a new panel (eg. games view or chat view)
	 *
	 * @param panel
	 *            The panel to use as content.
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
		getGameClient().getPlayerManager().addObserver(
				playersPanel.getTableModel());
		playersPanel.getTableModel().refreshData(
				getGameClient().getPlayerManager());
		// Same thing for the game descriptor manager
		getGameClient().getGameDescriptorManager().addObserver(
				gamesPanel.getTableModel());
		gamesPanel.getTableModel().refreshData(
				getGameClient().getGameDescriptorManager());
	}

	@Override
	public void exit() {
		// When we leave this screen, we don't want to receive updates
		// from the player manager anymore.
		getGameClient().getPlayerManager().deleteObserver(
				playersPanel.getTableModel());
		// Same thing for the game descriptor manager
		getGameClient().getGameDescriptorManager().deleteObserver(
				gamesPanel.getTableModel());
		getGameClient().getGameDescriptorManager().deleteObserver(
			loadGamesPanel.getTableModel());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MessagePacket) {
			chatPanel.publishChatMessage((MessagePacket) arg);
		} else if (arg instanceof GenericError) {
			JOptionPane.showMessageDialog(null, arg);
		} else if (arg instanceof GameStartPacket) {
			// Switch screens
			getClientWindow().setPanel(
					new GamePanel(getGameClient(), getClientWindow()));
			return;
		} else if (arg instanceof GameDescriptorPlayerUpdatePacket) {
			GameDescriptorPlayerUpdatePacket packet = (GameDescriptorPlayerUpdatePacket)arg;

			// If we were the player joining, then switch to game setup view
			if (packet.name.equals(getGameClient().getPlayerName())) {
				if (packet.joined) {
					getClientWindow().setPanel(new NewGamePanel(getGameClient(), getClientWindow(), packet.id));
				}
			}
			// TODO: Handle the case where we are observing a game
		}
	}

	@Override
	public JComponent getOverlayComponent() {
		return inviteOverlayPanel;
		// return null;
	}

	private class NewGameActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			NewGameDialog d = new NewGameDialog(getClientWindow(), getGameClient());
		}
	}
}
