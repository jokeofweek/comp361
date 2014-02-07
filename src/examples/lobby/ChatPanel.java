package examples.lobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ChatPanel() {
		super(new BorderLayout());
		
		Dimension size = new Dimension(300, 600);
		setPreferredSize(size);
		setSize(size);
		
		String[] playerNames = {"Alexander the Great"};
		
		JList<String> openChats = new JList<String>(playerNames);
		JPanel openChatsPanel = new JPanel(new BorderLayout());
		openChatsPanel.add(openChats, BorderLayout.CENTER);
		openChatsPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 0));
		
		JTextArea conversation = new JTextArea();
		conversation.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel conversationPanel = new JPanel(new BorderLayout());
		conversationPanel.add(new JLabel("Chat"), BorderLayout.NORTH);
		conversationPanel.add(conversation, BorderLayout.CENTER);
		conversationPanel.add(new JTextField(), BorderLayout.SOUTH);
		
		add(openChatsPanel, BorderLayout.WEST);
		add(conversationPanel, BorderLayout.CENTER);
	}
}
