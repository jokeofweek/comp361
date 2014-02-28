package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang3.StringEscapeUtils;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
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
		
		this.gameClient = gameClient;
				
	    // Create the panel for sending a message
	    add(getChatPanel(), BorderLayout.CENTER);
	    add(getMessagePanel(), BorderLayout.SOUTH);
	    SwagFactory.style(this);
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
	    container.add(chatScrollPane);
	    container.setBorder(BorderFactory.createEmptyBorder(CHAT_BOX_SPACING, CHAT_BOX_SPACING, 0, CHAT_BOX_SPACING));
	    SwagFactory.style(container);	
	    
	    return container;
	}
	
	private JComponent getMessagePanel() {
		JPanel messagePanel = new JPanel(new BorderLayout(CHAT_BOX_SPACING, 0));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(
				CHAT_BOX_SPACING, CHAT_BOX_SPACING, CHAT_BOX_SPACING, CHAT_BOX_SPACING));
		
		
		SendChatAction action = new SendChatAction();
		
		// Build the send button
		JButton sendButton = new JButton(action);
		sendButton.setText("Send");
		SwagFactory.style(sendButton);
		
		Dimension d = new Dimension(120, SwagFactory.BUTTON_HEIGHT);
		sendButton.setMaximumSize(d);
		sendButton.setMinimumSize(d);
		sendButton.setPreferredSize(d);
		sendButton.setSize(d);
		
		// Create the message field
		messageField = new JTextField();
		messageField.setAction(action);
		
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		SwagFactory.style(messagePanel);
		
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
				
				// Add the sender if it's there.
				if (packet.senderName != null) {
					message += "<b>" + StringEscapeUtils.escapeHtml4(packet.senderName) + "</b>: ";
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
				chatEditorPane.setCaretPosition(chatEditorPane.getDocument().getLength());
			}
		});
	}
	
	
	
	private class SendChatAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			// If we have a message, we want to send it
			
			if (!messageField.getText().isEmpty()) {
				// Send the message
				MessagePacket messagePacket = new MessagePacket();
				messagePacket.message = messageField.getText();
				messagePacket.senderName = gameClient.getPlayerName();
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
		}
		
	}
}
