package comp361.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comp361.shared.packets.shared.MessagePacket;

public class LobbyWindow extends JFrame implements Observer {

	private static final long serialVersionUID = 7525404399381734980L;

	private GameClient gameClient;

	private JTextArea chatTextArea;
	private JTextField inputTextField;

	public LobbyWindow(GameClient gameClient) {
		this.gameClient = gameClient;

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(getChatPanel(), BorderLayout.CENTER);
		mainPanel.add(getInputPanel(), BorderLayout.SOUTH);
		addWindowListener(getWindowListener());
		add(mainPanel);

		setTitle("Battleships Lobby");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Subscribe to the game client
		gameClient.addObserver(this);
	}

	/**
	 * @return the panel with the chat window.
	 */
	private JPanel getChatPanel() {
		JPanel panel = new JPanel();
		chatTextArea = new JTextArea(30, 80);
		panel.add(chatTextArea);
		return panel;
	}

	/**
	 * @return the panel with the input field and the send button.
	 */
	private JPanel getInputPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		inputTextField = new JTextField(60);
		panel.add(inputTextField);

		JButton sendButton = new JButton("Send");
		panel.add(sendButton, BorderLayout.EAST);

		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Try to send the message if there is any.
				if (inputTextField.getText().length() > 0) {
					MessagePacket packet = new MessagePacket();
					packet.message = inputTextField.getText();
					gameClient.getClient().sendTCP(packet);
					inputTextField.setText("");
					inputTextField.requestFocus();
					// Add the message directly to our own screen
					addChatMessage(packet.message);
				}
			}
		});

		return panel;
	}

	/**
	 * @return the window listener.
	 */
	private WindowListener getWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Shut down the client on window closing.
				gameClient.getClient().stop();
			}
		};

	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		 * Any message sent through GameClient.publishMesage will go here.
		 */
		if (arg instanceof MessagePacket) {
			addChatMessage(((MessagePacket) arg).message);
		}
	}

	/**
	 * Add a chat message to the message screen.
	 * 
	 * @param message
	 *            The message to add.
	 */
	private void addChatMessage(final String message) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				chatTextArea.setText(chatTextArea.getText() + "\n" + message);
			}
		});
	}
}
