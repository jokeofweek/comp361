package comp361.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartWindow extends JFrame {

	private static final long serialVersionUID = -4930439165369237418L;
	private JTextField hostTextField;
	private JTextField portTextField;

	public StartWindow() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(getLabelsPanel(), BorderLayout.WEST);
		mainPanel.add(getFieldsPanel(), BorderLayout.CENTER);
		mainPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		add(mainPanel);

		setTitle("Battleships!");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * @return the panel with the field labels.
	 */
	private JPanel getLabelsPanel() {
		JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		labelsPanel.add(new JLabel("Host:"));
		labelsPanel.add(new JLabel("Port:"));
		return labelsPanel;
	}

	/**
	 * @return the panel with the host and port field.
	 */
	private JPanel getFieldsPanel() {

		JPanel fieldsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		hostTextField = new JTextField(40);
		fieldsPanel.add(hostTextField);
		portTextField = new JTextField(40);
		fieldsPanel.add(portTextField);

		return fieldsPanel;
	}

	/**
	 * @return the panel with the connect button.
	 */
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		JButton connectButton = new JButton("Connect");
		panel.add(connectButton);

		final StartWindow window = this;

		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate date (sort of...)
				String host = hostTextField.getText();
				int port = 4000;
				try {
					port = Integer.parseInt(portTextField.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"The port must be numberic.");
					return;
				}
				// Try to create the client
				try {
					GameClient gameClient = new GameClient(host, port);
					// If successful, hide this window and show the lobby
					// window.
					window.setVisible(false);
					new LobbyWindow(gameClient).setVisible(true);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,
							"An error occured connecting to the server.");
					ex.printStackTrace();
				}
			}
		});

		return panel;
	}

}
