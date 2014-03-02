package comp361.client.ui.setup;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.shared.ChangeSeedPacket;

public class NewGamePanel extends ClientPanel {

	private CoralReefGenerator reefGenerator;
	private GameInfoPanel infoPanel;
	private int gameDescriptorId;
	private JButton readyButton;
	private ReadyActionListener readyActionListener;

	private static final int COMPONENT_SPACING = 5;

	public NewGamePanel(GameClient gameClient, ClientWindow window,
			int gameDescriptorId) {
		super(gameClient, window, new BorderLayout());

		this.gameDescriptorId = gameDescriptorId;
		this.reefGenerator = new CoralReefGenerator();
		this.reefGenerator.regenerateReef(gameClient.getGameDescriptorManager()
				.getGameDescriptor(gameDescriptorId).getSeed());

		// Setup the UI
		this.add(this.buildCoralUI(), BorderLayout.WEST);
		this.add(this.buildOptionsUI());

		SwagFactory.style(this);
	}

	private JPanel buildCoralUI() {
		// Wrap the coral panel in another panel so that we can put a border on
		JPanel coralContainer = new JPanel(new BorderLayout());
		SwagFactory.style(coralContainer);

		// Build the panel which actually holds the coral reef.
		CoralPanel coralPanel = new CoralPanel(reefGenerator);
		coralContainer.add(coralPanel);

		// Build the containers for the buttons
		JPanel buttonContainer = new JPanel(new GridLayout(2, 1,
				COMPONENT_SPACING, COMPONENT_SPACING));
		SwagFactory.style(buttonContainer);

		// Build the button for regenerating the coral
		JButton regenerateCoralButton = new JButton("Regenerate Corals");
		regenerateCoralButton
				.addActionListener(new RegenerateCoralsActionListener());
		buttonContainer.add(regenerateCoralButton);
		SwagFactory.style(regenerateCoralButton);

		JPanel horizontalButtonContainer = new JPanel(new GridLayout(1, 2,
				COMPONENT_SPACING, 0));

		// build the leave button
		JButton leaveButton = new JButton();
		leaveButton.setText("Leave");
		SwagFactory.style(leaveButton);
		horizontalButtonContainer.add(leaveButton);
		leaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Build the leave packet
				LeaveGamePacket packet = new LeaveGamePacket();
				getGameClient().getClient().sendTCP(packet);
			}
		});

		// Build the ready button
		readyButton = new JButton("Ready");
		readyActionListener = new ReadyActionListener();
		readyButton.addActionListener(readyActionListener);
		SwagFactory.style(readyButton);
		horizontalButtonContainer.add(readyButton);

		buttonContainer.add(horizontalButtonContainer);
		coralContainer.add(buttonContainer, BorderLayout.SOUTH);

		// Wrap the coral panel container with spacing since apparently
		// you can't put a border on CoralPanel?
		coralContainer.setBorder(BorderFactory.createEmptyBorder(
				COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING,
				COMPONENT_SPACING));
		return coralContainer;
	}

	private JPanel buildOptionsUI() {
		JPanel optionsContainer = new JPanel(new BorderLayout());

		infoPanel = new GameInfoPanel(getGameClient()
				.getGameDescriptorManager(), gameDescriptorId);
		optionsContainer.add(infoPanel, BorderLayout.NORTH);

		optionsContainer.add(buildChatUI());

		optionsContainer.setBorder(BorderFactory.createEmptyBorder(
				COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING,
				COMPONENT_SPACING));

		return optionsContainer;
	}

	private JPanel buildChatUI() {
		JPanel chatContainer = new JPanel(new BorderLayout());

		chatContainer.add(new JTextArea());
		chatContainer.add(new JTextField(), BorderLayout.SOUTH);

		return chatContainer;
	}

	@Override
	public void enter() {
		getGameClient().getGameDescriptorManager().addObserver(infoPanel);
	}

	@Override
	public void exit() {
		getGameClient().getGameDescriptorManager().deleteObserver(infoPanel);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof GameDescriptorPlayerUpdatePacket) {
			GameDescriptorPlayerUpdatePacket packet = (GameDescriptorPlayerUpdatePacket) arg;
			// If it was this game.
			if (packet.id == gameDescriptorId) {
				// If it was us, go back to the lobby
				if (packet.name.equals(getGameClient().getPlayerName())
						&& !packet.joined) {
					getClientWindow().setPanel(
							new LobbyPanel(getGameClient(), getClientWindow()));
				}
			}
		} else if (arg instanceof ChangeSeedPacket) {
			// Update the seed of the generator if this is our game
			if (((ChangeSeedPacket) arg).id == gameDescriptorId) {
				reefGenerator.regenerateReef(((ChangeSeedPacket) arg).seed);
			}
		}

		// Refresh ready action listener's data.
		if (getGameClient().getGameDescriptorManager().getGameDescriptor(
				gameDescriptorId) != null) {
			readyActionListener.updateStatus(getGameClient()
					.getGameDescriptorManager()
					.getGameDescriptor(gameDescriptorId).getReadyPlayers()
					.contains(getGameClient().getPlayerName()));
		}
	}

	private class RegenerateCoralsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Regenerate the reef
			reefGenerator.regenerateReef();
			// Create the packet with the new seed
			ChangeSeedPacket packet = new ChangeSeedPacket();
			packet.id = gameDescriptorId;
			packet.seed = reefGenerator.getSeed();
			getGameClient().getClient().sendTCP(packet);
		}
	}

	private class ReadyActionListener implements ActionListener {
		private boolean isReady = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			// Toggle the ready status
			updateStatus(!isReady);

			// Send the packet
			UpdateReadyPacket packet = new UpdateReadyPacket();
			packet.ready = isReady;
			getGameClient().getClient().sendTCP(packet);

		}

		public void updateStatus(boolean isReady) {
			this.isReady = isReady;
			// Set the message
			readyButton.setText(isReady ? "Cancel Ready" : "Ready");

		}
	}

}
