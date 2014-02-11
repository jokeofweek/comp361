package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import comp361.client.ui.SwagFactory;

public class ChatPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int CHAT_BOX_SPACING = LobbyPanel.COMPONENT_SPACING;
	
	public ChatPanel() {
		super(new BorderLayout());
		SwagFactory.style(this);
		
		// Create the text field
	    final HTMLEditorKit kit = new HTMLEditorKit();
	    final HTMLDocument doc = new HTMLDocument();
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		
	    editorPane.setEditorKit(kit);
	    editorPane.setDocument(doc);
	    try {
	    	kit.insertHTML(doc, doc.getLength(), "<b>Dominic:</b> Hello, world!<br/>Test", 0, 0, null);
	    	kit.insertHTML(doc, doc.getLength(), "<br/><b>Alex:</b> Stahp. pls.", 0, 0, null);
	    } catch (Exception e) {}

	    new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					kit.insertHTML(doc, doc.getLength(), "<b>Time:</b>" + System.currentTimeMillis() + " It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).", 0, 0, null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}).start();
	    
	    // Wrap the editor pane in a scroll pane
	    JScrollPane scrollPane = new JScrollPane(editorPane, 
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    JPanel container = new JPanel(new BorderLayout());
	    SwagFactory.style(container);
	    container.add(scrollPane);
	    container.setBorder(BorderFactory.createEmptyBorder(CHAT_BOX_SPACING, CHAT_BOX_SPACING, 0, CHAT_BOX_SPACING));
	   
	    // Create the panel for sending a message
	    add(container, BorderLayout.CENTER);
	    add(getMessagePanel(), BorderLayout.SOUTH);
	}
	
	private JComponent getMessagePanel() {
		JPanel messagePanel = new JPanel(new BorderLayout(CHAT_BOX_SPACING, 0));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(
				CHAT_BOX_SPACING, CHAT_BOX_SPACING, CHAT_BOX_SPACING, CHAT_BOX_SPACING));
		
		SwagFactory.style(messagePanel);
		
		// Build the send button
		JButton sendButton = new JButton("Send");
		SwagFactory.style(sendButton);
		
		Dimension d = new Dimension(80, SwagFactory.BUTTON_HEIGHT);
		sendButton.setMaximumSize(d);
		sendButton.setMinimumSize(d);
		sendButton.setPreferredSize(d);
		sendButton.setSize(d);
		
		messagePanel.add(new JTextField(), BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		
		return messagePanel;
		
	}
}
