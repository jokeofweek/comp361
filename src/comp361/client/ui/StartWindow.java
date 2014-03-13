package comp361.client.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import comp361.client.GameClient;
import comp361.client.resources.BackgroundPlayer;
import comp361.client.resources.ResourceManager;
import comp361.shared.Constants;

/**
 * This window sets up the connection between the client and the server.
 */
public class StartWindow extends JFrame {

	private static final long serialVersionUID = -4930439165369237418L;
	private JTextField hostTextField;
	private JTextField portTextField;
	private JButton connectButton;
	private JButton remoteButton;
	
	private static final String REMOTE_IP = "107.170.48.190";
	private static boolean USE_REMOTE = false;
	
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
		
		getRootPane().setDefaultButton(connectButton);
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
		hostTextField.setText("127.0.0.1");
		if (USE_REMOTE) {
			hostTextField.setText(REMOTE_IP);
		}
		
		portTextField = new JTextField(40);
		portTextField.setText(Constants.PORT + "");
		fieldsPanel.add(hostTextField);
		fieldsPanel.add(portTextField);
		
		hostTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// ctrl + r -> connect to remote
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R) {
					remoteButton.doClick();
				}
			}
		});

		return fieldsPanel;
	}

	/**
	 * @return the panel with the connect button.
	 */
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		connectButton = new JButton("Connect");
		panel.add(connectButton);
		
		remoteButton = new JButton("Remote");
		panel.add(remoteButton);

		final StartWindow window = this;

		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate data (sort of...)
				String host = hostTextField.getText();
				int port = Constants.PORT;
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
					
					ClientWindow clientWindow = new ClientWindow(gameClient);
					// Load the resources
					ResourceManager.getInstance();
					// Create the login window
					clientWindow.setPanel(new LoginPanel(gameClient, clientWindow));

					// Play background music
					startBackgroundMusic();

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null,
							"An error occured connecting to the server.");
				}
			}
		});
		
		remoteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostTextField.setText(REMOTE_IP);
				connectButton.doClick();
			}
		});

		return panel;
	}

	private void startBackgroundMusic() {
		new Thread(new Runnable() {
			public void run() {
				new BackgroundPlayer().play();
			}
		}).start();
	}
}
