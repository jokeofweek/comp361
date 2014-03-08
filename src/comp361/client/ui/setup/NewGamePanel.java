package comp361.client.ui.setup;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Observable;
import java2D.GamePanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang3.StringEscapeUtils;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.shared.ChangeSeedPacket;
import comp361.shared.packets.shared.SetupMessagePacket;

public class NewGamePanel extends ClientPanel {

	private CoralReefGenerator reefGenerator;
	private GameInfoPanel infoPanel;
	private int gameDescriptorId;
	private JButton readyButton;
	private ReadyActionListener readyActionListener;

	private JScrollPane chatScrollPane;
	private JEditorPane chatEditorPane;
	private JTextField messageField;
	private HTMLEditorKit kit;
	private HTMLDocument doc;

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

		JLabel chatLabel = new JLabel("Chat Area:");
		chatContainer.add(chatLabel, BorderLayout.BEFORE_FIRST_LINE);

		chatContainer.add(getChatPanel(), BorderLayout.CENTER);

		// Build the containers for the buttons
		JPanel buttonContainer = new JPanel(new GridLayout(1, 1,
				COMPONENT_SPACING, COMPONENT_SPACING));

		// The send text field
		final JTextField chatField = new JTextField("Type some stuff");
		chatField.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				chatField.setText("");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		buttonContainer.add(chatField);

		// The send button
		final JButton sendButton = new JButton("Send");

		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SetupMessagePacket packet = new SetupMessagePacket();
				packet.message = chatField.getText();
				packet.senderName = (getGameClient().getPlayerName());

				getGameClient().getClient().sendTCP(packet);
				// publishChatMessage(packet);

				chatField.setText("");
			}
		});

		// Add the send button
		buttonContainer.add(sendButton);

		chatContainer.add(buttonContainer, BorderLayout.SOUTH);
		return chatContainer;
	}

	private JComponent getChatPanel() {
		// Create the text field
		kit = new HTMLEditorKit();
		doc = new HTMLDocument();
		SwagFactory.style(chatEditorPane);
		chatEditorPane = new JEditorPane();
		chatEditorPane.setEditable(false);
		chatEditorPane.setEditorKit(kit);
		chatEditorPane.setDocument(doc);
		try {
			kit.insertHTML(doc, doc.getLength(),
					"Please chat with your opponent!", 0, 0, null);
		} catch (Exception e) {
		}

		// Wrap the editor pane in a scroll pane
		chatScrollPane = new JScrollPane(chatEditorPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel container = new JPanel(new BorderLayout());
		SwagFactory.style(container);
		container.add(chatScrollPane);

		return container;
	}

	public void publishChatMessage(final SetupMessagePacket packet) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String message = "";

				// We have to escape both the sender name and the message just
				// to make sure there's
				// no HTML in there that shouldn't be there. This will make it
				// so <b> gets shown as
				// is instead of bolding text.

				// Add the sender if it's there.
				if (packet.senderName != null) {
					message += "<b>"
							+ StringEscapeUtils.escapeHtml4(packet.senderName)
							+ "</b>: ";
				}
				message += StringEscapeUtils.escapeHtml4(packet.message);
				// Insert the message then scroll to the bottom.
				try {

					kit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Temporary hack, could be better..
				chatEditorPane.setCaretPosition(chatEditorPane.getDocument()
						.getLength());
			}
		});
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
		} else if (arg instanceof GameStartPacket) {
			// Switch screens
			getClientWindow().setPanel(
					new GamePanel(getGameClient(), getClientWindow()));
			return;
		}

		// Refresh ready action listener's data.
		if (getGameClient().getGameDescriptorManager().getGameDescriptor(
				gameDescriptorId) != null) {
			readyActionListener.updateStatus(getGameClient()
					.getGameDescriptorManager()
					.getGameDescriptor(gameDescriptorId).getReadyPlayers()
					.contains(getGameClient().getPlayerName()));
		}

		// Handle the packet
		if (arg instanceof SetupMessagePacket) {
			publishChatMessage((SetupMessagePacket) arg);
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
