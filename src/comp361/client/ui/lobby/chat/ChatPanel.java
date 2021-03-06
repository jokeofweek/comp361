package comp361.client.ui.lobby.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang3.StringEscapeUtils;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.Constants;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.shared.MessagePacket;

public class ChatPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int CHAT_BOX_SPACING = LobbyPanel.COMPONENT_SPACING;
	
	private JScrollPane chatScrollPane;
	private JEditorPane chatEditorPane;
	private JTextField messageField;
	private HTMLEditorKit kit;
	private HTMLDocument doc;
	
	private GameClient gameClient;
	
	public ChatPanel(GameClient gameClient) {
		super(new BorderLayout());
		SwagFactory.style(this);
		
		this.gameClient = gameClient;
				
	    // Create the panel for sending a message
	    add(getChatPanel(), BorderLayout.CENTER);
	    add(getMessagePanel(), BorderLayout.SOUTH);
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
	    	kit.insertHTML(doc, doc.getLength(), "<b>Welcome to Battleships!</b>", 0, 0, null);
	    } catch (Exception e) {}


	    // Wrap the editor pane in a scroll pane
	    chatScrollPane = new JScrollPane(chatEditorPane, 
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    JPanel container = new JPanel(new BorderLayout());
	    SwagFactory.style(container);	    
	    container.add(chatScrollPane);
	    
	    return container;
	}
	
	private JComponent getMessagePanel() {
		JPanel messagePanel = new JPanel(new BorderLayout(CHAT_BOX_SPACING, 0));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(
				CHAT_BOX_SPACING, 0, 0, 0));
		
		SwagFactory.style(messagePanel);
		
		SendChatAction action = new SendChatAction();
		
		// Build the send button
		JButton sendButton = new JButton(action);
		sendButton.setText("Send");
		SwagFactory.style(sendButton);

		// Remove preferred size set by SwagFactory
		sendButton.setPreferredSize(null);
		
		// Create the message field
		messageField = new JTextField();
		messageField.setAction(action);
		
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		messageField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (!e.isControlDown() || (e.getKeyCode() != KeyEvent.VK_1 && e.getKeyCode() != KeyEvent.VK_2)) {
					return;
				}

				sendNewGamePacket("" + System.currentTimeMillis(), "", e.getKeyCode() == KeyEvent.VK_1 ? 0 : 1);
			}
		});

		return messagePanel;
	}
	
	public JTextField getMessageField() {
		return messageField;
	}
	
	public void publishChatMessage(final MessagePacket packet) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String message = "";
				
				// We have to escape both the sender name and the message just to make sure there's
				// no HTML in there that shouldn't be there. This will make it so <b> gets shown as 
				// is instead of bolding text.
				
				// Check if message is image URL
				boolean isImageURL = true;

				try {
					URL url = new URL(packet.message);
					isImageURL = url.getPath().matches(".*\\.(png|gif|jpg|jpeg)");
				} catch (Exception e) {
					isImageURL = false;
				}

				if (packet.isMetaMessage) {
					// Message is a self action sent with "/me"
					message += "<i>";
					if (packet.senderName != null) {
						message += StringEscapeUtils.escapeHtml4(packet.senderName + " ");
					}
					message += StringEscapeUtils.escapeHtml4(packet.message);
					message += "</i>";
				} else {
					// Add the sender if it's there.
					if (packet.senderName != null) {
						message += "<b>" + StringEscapeUtils.escapeHtml4(packet.senderName) + "</b>: ";
					}

					if (isImageURL) {
						message += "<br><img src=\"" + packet.message + "\">";
					} else {
						message += StringEscapeUtils.escapeHtml4(packet.message);
					}
				}
				
				// Insert the message then scroll to the bottom.
				try {
					kit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Temporary hack, could be better..
				chatEditorPane.setCaretPosition(chatEditorPane.getDocument().getLength());
			}
		});
	}
	
	private void sendNewGamePacket(String name, String password, int shipInventory) {
		NewGameDescriptorPacket packet = new NewGameDescriptorPacket();
		packet.maxPlayers = Constants.NUM_PLAYERS;
		packet.name = name;
		packet.password = password;
		packet.shipInventory = shipInventory;
		gameClient.getClient().sendTCP(packet);
	}
	
	private class SendChatAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = messageField.getText();
			boolean isMetaMessage = false;
			
			// Bail if message is empty
			if (message.isEmpty()) {
				return;
			}
			
			// Check for '/' commands
			if (message.charAt(0) == '/' && message.length() > 1) {
				// Remote '/' from beginning of message
				message = message.substring(1);
				
				// Tokenize message
				List<String> tokens = new ArrayList<String>(Arrays.asList(message.split(" ")));
				String command = tokens.remove(0);
				int numTokens = tokens.size();
				
				if (command.equals("me") && numTokens > 0) {
					message = join(tokens);
					isMetaMessage = true;
				} else if (command.equals("newgame")) {
					// Bail if not enough tokens
					if (numTokens != 1 && numTokens != 2) {
						return;
					}

					String name = tokens.remove(0);
					String password = "";

					if (numTokens == 2) {
						password = tokens.remove(0);
					}

					sendNewGamePacket(name, password, 0);
					return;
				} else {
					// Ignore any unrecognized commands
					messageField.setText("");
					
					// Inform player of invalid command
					MessagePacket invalidCommandPacket = new MessagePacket();
					invalidCommandPacket.message = "\"" + command + "\" is not a valid command";
					invalidCommandPacket.isMetaMessage = true;
					publishChatMessage(invalidCommandPacket);
					return;
				}
			}

			// Send the message
			MessagePacket messagePacket = new MessagePacket();
			messagePacket.message = message;
			messagePacket.senderName = gameClient.getPlayerName();
			messagePacket.isMetaMessage = isMetaMessage;
			gameClient.getClient().sendTCP(messagePacket);
			publishChatMessage(messagePacket);
			
			// Clear the message field
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					messageField.setText("");
					messageField.requestFocusInWindow();
				}
			});
		}

		private String join(List<String> words) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < words.size(); i++) {
				sb.append(words.get(i));
				if (i != words.size() - 1) {
					sb.append(" ");
				}
			}
			return sb.toString();
		}
	}
}
