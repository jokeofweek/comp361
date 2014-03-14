package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang3.StringEscapeUtils;

import comp361.client.GameClient;
import comp361.client.data.EventTooltipContext;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.client.ui.util.HealthBar;
import comp361.shared.Constants;
import comp361.shared.data.GameResult;
import comp361.shared.data.Ship;
import comp361.shared.packets.shared.GameOverPacket;
import comp361.shared.packets.shared.InGameMessagePacket;
import comp361.shared.packets.shared.MessagePacket;

public class GamePanel extends ClientPanel {
	
	private EventTooltipPanel tooltipPanel;
	
	private SelectionContext context;
	private EventTooltipContext eventContext;
	
	private JLabel turnLabel;
	private GameFieldPanel fieldPanel;
	private ShipInfoPanel infoPanel;
	private List<HealthBar> healthBars;
	
	private JScrollPane chatScrollPane;
	private JEditorPane chatEditorPane;
	private HTMLEditorKit kit;
	private HTMLDocument doc;
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		
		initUI(gameClient);
		
		// Add this as an observer of the context
		context.addObserver(this);
		tooltipPanel = new EventTooltipPanel(eventContext);
	}

	private void initUI(final GameClient client) {
		setLayout(new BorderLayout());

		context = new SelectionContext();
		eventContext = new EventTooltipContext(getGameClient().getGameManager().isPlayer1());
		
		fieldPanel = new GameFieldPanel(client, context, client.getGameManager().isPlayer1(), eventContext);
		add(fieldPanel, BorderLayout.CENTER);
		
		JPanel leftBarPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		
		// Container for the turn info and health info
		JPanel gameInfoPanel = new JPanel(new BorderLayout());
		turnLabel = new JLabel();
		turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		updateTurnLabel();
		gameInfoPanel.add(turnLabel, BorderLayout.NORTH);
		
		// Buid health bars for every player
		JPanel healthPanel = new JPanel(new GridLayout(4, 1));
		healthBars = new ArrayList<>();
		for (final String player : new String[]{client.getGameManager().getGame().getP1(), client.getGameManager().getGame().getP2()}) {
			healthPanel.add(new JLabel(player + ":"));
			HealthBar bar = (new HealthBar(10) {
				
				@Override
				public int getValue() {
					int remainingHealth = 0;
					for (Ship s : client.getGameManager().getGame().getPlayerShips(player)) {
						for (int i = 0; i < s.getSize(); i++) {
							remainingHealth += s.getHealth(i);
						}
					}
					return remainingHealth;
				}
				
				@Override
				public int getMaxValue() {
					int totalHealth = 0;
					for (Ship s : client.getGameManager().getGame().getPlayerShips(player)) {
						totalHealth += s.getMaxHealthPerSquare() * s.getSize();
					}
					return totalHealth;
				}
			});
			healthBars.add(bar);
			healthPanel.add(bar);
		}
		
		gameInfoPanel.add(healthPanel);
		topPanel.add(gameInfoPanel, BorderLayout.NORTH);
		
		// Container for the ship info
		infoPanel = new ShipInfoPanel(client, context);
		topPanel.add(infoPanel, BorderLayout.CENTER);

		Dimension d = new Dimension(Constants.SCREEN_WIDTH - (Constants.MAP_WIDTH * Constants.TILE_SIZE), 2*(Constants.SCREEN_HEIGHT / 3));
		topPanel.setSize(d);
		topPanel.setPreferredSize(d);
		topPanel.setMaximumSize(d);
		topPanel.setMinimumSize(d);
		
		leftBarPanel.add(topPanel, BorderLayout.NORTH);
		leftBarPanel.add(buildChatUI());
		
		d = new Dimension(Constants.SCREEN_WIDTH - (Constants.MAP_WIDTH * Constants.TILE_SIZE), Constants.SCREEN_HEIGHT);
		leftBarPanel.setSize(d);
		leftBarPanel.setPreferredSize(d);
		leftBarPanel.setMaximumSize(d);
		leftBarPanel.setMinimumSize(d);
		 
		add(leftBarPanel, BorderLayout.WEST );
	}
	
	private JPanel buildChatUI() {
		JPanel container = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(1, 2));

		// Message text field
		final JTextField messageField = new JTextField("Chat here...");
		messageField.setForeground(Color.GRAY);
		messageField.addMouseListener(new MouseAdapter() {
			private boolean cleared = false;

			public void mousePressed(MouseEvent e) {
				if (!cleared) {
					messageField.setText("");
					messageField.setForeground(Color.BLACK);
				}
			}
		});

		// Message send button
		final JButton sendButton = new JButton("Send");

		// Sends message when triggered
		ActionListener messageSendHandler = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = messageField.getText();

				// Avoid sending default text or empty message
				if (messageField.getForeground() == Color.GRAY
						|| message.isEmpty()) {
					return;
				}

				InGameMessagePacket packet = new InGameMessagePacket();
				packet.message = message;
				packet.senderName = getGameClient().getPlayerName();

				// Send message packet
				getGameClient().getClient().sendTCP(packet);
				publishChatMessage(packet);
				
				// Clear message text field
				messageField.setText("");
			}
		};

		// Attach message send handler
		sendButton.addActionListener(messageSendHandler);
		messageField.addActionListener(messageSendHandler);

		inputPanel.add(messageField);
		inputPanel.add(sendButton);
		SwagFactory.styleButtonWithoutHeight(sendButton);

		container.add(getChatPanel(), BorderLayout.CENTER);
		container.add(inputPanel, BorderLayout.SOUTH);

		return container;
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
//		try {
//			kit.insertHTML(doc, doc.getLength(),
//					"Please chat with your opponent!", 0, 0, null);
//		} catch (Exception e) {
//		}

		// Wrap the editor pane in a scroll pane
		chatScrollPane = new JScrollPane(chatEditorPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel container = new JPanel(new BorderLayout());
		SwagFactory.style(container);
		container.add(chatScrollPane);
		return container;
	}
	

	public void publishChatMessage(final MessagePacket packet) {
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
		getGameClient().getGameManager().addObserver(this);
	}
	
	@Override
	public void exit() {
		getGameClient().getGameManager().deleteObserver(this);
	}
	
	private void updateTurnLabel() {
		turnLabel.setText(getGameClient().getGameManager().isTurn() ? "Your turn" : "Enemy's turn");
	}
	
	@Override
	public void update(Observable source, Object object) {
		// If we received a game over packet
		if (object instanceof GameOverPacket) {
			// As long as we didn't disconnect (and therefore lose), show a message
			GameOverPacket packet = (GameOverPacket) object;
			if (packet.result != GameResult.LOSS || !packet.fromDisconnect) {
				String message = packet.result == GameResult.WIN ? "You won!" : "You lost!";
				if (packet.message != null) {
					message += " " + packet.message;
				}
				JOptionPane.showMessageDialog(null, message);
				getClientWindow().setPanel(new LobbyPanel(getGameClient(), getClientWindow()));
			}
			return;
		} else if (object instanceof InGameMessagePacket) {
			publishChatMessage((MessagePacket)object);
			return;
		}
		
		// If it is no longer our turn, clear the context
		if (context.getShip() != null && context.getType() != null && source instanceof GameManager) {
			if (!((GameManager)source).isTurn()) {
				context.setShip(null);
			}
		}
		// If the ship in the context is sunk, clear it
		if (context.getShip() != null && context.getShip().isSunk()) {
			context.setShip(null);
		}
		// If a turn passed, update the context points
		if (source instanceof GameManager) {
			context.updatePoints();
		}
		// Update the info panel
		updateTurnLabel();
		
		// Notify observers
		infoPanel.update(source, object);
		fieldPanel.update(source, object);
		for (HealthBar bar : healthBars) {
			bar.update(source, object);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				revalidate();
				repaint();
			}
		});
	}
	
	@Override
	public JComponent getOverlayComponent() {
		return tooltipPanel;
	}

	
	
}
