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
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.shared.ChangeSeedPacket;

public class NewGamePanel extends ClientPanel {

	private CoralReefGenerator reefGenerator;
	private GameInfoPanel infoPanel;
	private ReadyAction readyAction;
	private int gameDescriptorId;

	private static final int COMPONENT_SPACING = 5;

	public NewGamePanel(GameClient gameClient, ClientWindow window,
			int gameDescriptorId) {
		super(gameClient, window, new BorderLayout());

		this.gameDescriptorId = gameDescriptorId;
		this.reefGenerator = new CoralReefGenerator();
		this.reefGenerator.regenerateReef(gameClient.getGameDescriptorManager()
				.getGameDescriptor(gameDescriptorId).getSeed());
		this.readyAction = new ReadyAction();

		// Setup the UI
		this.add(this.buildCoralUI(), BorderLayout.WEST);
		this.add(this.buildOptionsUI());

		// Start off with the ready action disabled.
		updateActionStatuses();

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
		JButton readyButton = new JButton(readyAction);
		readyButton.setText("Ready");
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

	private void updateActionStatuses() {
		// Make sure the game still exists, as this gets called when the player
		// leaves the game descriptor.
		if (getGameClient().getGameDescriptorManager().getGameDescriptor(
				gameDescriptorId) == null) {
			return;
		}

		// Update the ready action
		GameDescriptor descriptor = getGameClient().getGameDescriptorManager()
				.getGameDescriptor(gameDescriptorId);

		readyAction.setEnabled(descriptor.getPlayers().size() == descriptor
				.getMaxPlayers());
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

		updateActionStatuses();
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

}
