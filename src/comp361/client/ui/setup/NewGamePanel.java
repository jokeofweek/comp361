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
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;

public class NewGamePanel extends ClientPanel {

	private CoralReefGenerator reefGenerator;
	
	private RegenerateCoralAction regenerateCoralAction;
	private ReadyAction readyAction;
	private int gameDescriptorId;
		
	private static final int COMPONENT_SPACING = 5;
	
	public NewGamePanel(GameClient gameClient, ClientWindow window, int gameDescriptorId) {
		super(gameClient, window, new BorderLayout());
		
		this.reefGenerator = new CoralReefGenerator();
		this.regenerateCoralAction = new RegenerateCoralAction(reefGenerator);
		this.readyAction = new ReadyAction(regenerateCoralAction);
		this.gameDescriptorId = gameDescriptorId;
		
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
		JPanel buttonContainer = new JPanel(new GridLayout(2, 1, COMPONENT_SPACING, COMPONENT_SPACING));
		SwagFactory.style(buttonContainer);
		
		// Build the button for regenerating the coral
		JButton regenerateCoralButton = new JButton(regenerateCoralAction);
		regenerateCoralButton.setText("Regenerate Corals");
		buttonContainer.add(regenerateCoralButton);
		SwagFactory.style(regenerateCoralButton);
		
		JPanel horizontalButtonContainer = new JPanel(new GridLayout(1, 2, COMPONENT_SPACING, 0));

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
				COMPONENT_SPACING,
				COMPONENT_SPACING,
				COMPONENT_SPACING,
				COMPONENT_SPACING));
		return coralContainer;
	}
	
	private JPanel buildOptionsUI() {
		JPanel optionsContainer = new JPanel(new BorderLayout());
		
		optionsContainer.add(buildChatUI());
		

		optionsContainer.setBorder(BorderFactory.createEmptyBorder(
				COMPONENT_SPACING,
				COMPONENT_SPACING,
				COMPONENT_SPACING,
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
	public void update(Observable o, Object arg) {
		if (arg instanceof GameDescriptorPlayerUpdatePacket) {
			GameDescriptorPlayerUpdatePacket packet = (GameDescriptorPlayerUpdatePacket) arg;
			// If it was this game.
			if (packet.id == gameDescriptorId) {
				// If it was us, go back to the lobby
				if (packet.name.equals(getGameClient().getPlayerName())) {
					getClientWindow().setPanel(new LobbyPanel(getGameClient(), getClientWindow()));
				}
			}
		}
	}

	
}
