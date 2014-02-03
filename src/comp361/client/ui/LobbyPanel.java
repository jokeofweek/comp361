package comp361.client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comp361.client.GameClient;
import comp361.shared.packets.shared.MessagePacket;

public class LobbyPanel extends ClientPanel {

	private static final long serialVersionUID = 7525404399381734980L;

	private JTextArea chatTextArea;
	private JTextField inputTextField;

	public LobbyPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		this.add(getChatPanel(), BorderLayout.CENTER);
		this.add(getInputPanel(), BorderLayout.SOUTH);
	}

	/**
	 * @return the panel with the chat window.
	 */
	private JPanel getChatPanel() {
		JPanel panel = new JPanel();
		chatTextArea = new JTextArea(30, 60);
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
					getGameClient().getClient().sendTCP(packet);
					inputTextField.setText("");
					inputTextField.requestFocus();
					// Add the message directly to our own screen
					addChatMessage(packet.message);
				}
			}
		});

		return panel;
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
