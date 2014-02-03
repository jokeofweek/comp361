package comp361.client.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.LoginResult;
import comp361.shared.packets.server.RegisterResult;

public class LoginPanel extends ClientPanel {

	private static final long serialVersionUID = 7525404399381734980L;

	private JTextField loginUsernameField;
	private JTextField loginPasswordField;
	private JTextField registerUsernameField;
	private JTextField registerPasswordField;
	private JTextField registerConfirmPasswordField;

	public LoginPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new GridLayout(1, 2));
		this.add(getLoginPanel());
		this.add(getRegisterPanel());

	}

	/**
	 * @return the panel containing the login fields.
	 */
	private JPanel getLoginPanel() {
		JPanel loginPanel = new JPanel(new GridLayout(0, 1));

		loginPanel.add(new JLabel("Username:"));
		loginUsernameField = new JTextField();
		loginPanel.add(loginUsernameField);

		loginPanel.add(new JLabel("Password:"));
		loginPasswordField = new JPasswordField();
		loginPanel.add(loginPasswordField);
		
		// Waste space...
		loginPanel.add(new JLabel());
		loginPanel.add(new JLabel());

		JButton button = new JButton("Login");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure we have valid input
				if (loginUsernameField.getText().isEmpty()) {
					JOptionPane
							.showMessageDialog(null,
									"You must provide a username in order to login.");
				} else if (loginPasswordField.getText().isEmpty()) {
					JOptionPane
							.showMessageDialog(null,
									"You must provide a password in order to login.");
				} else {
					// Input has been validated, so send the login packet.
					LoginPacket packet = new LoginPacket();
					packet.accountName = loginUsernameField.getText();
					packet.password = loginPasswordField.getText();
					getGameClient().getClient().sendTCP(packet);
				}
			}
		});
		loginPanel.add(button);
		return loginPanel;
	}

	/**
	 * @return the panel containing the registration fields.
	 */
	private JPanel getRegisterPanel() {
		JPanel registerPanel = new JPanel(new GridLayout(0, 1));

		registerPanel.add(new JLabel("Username:"));
		registerUsernameField = new JTextField();
		registerPanel.add(registerUsernameField);

		registerPanel.add(new JLabel("Password:"));
		registerPasswordField = new JPasswordField();
		registerPanel.add(registerPasswordField);

		registerPanel.add(new JLabel("Confirm Password:"));
		registerConfirmPasswordField = new JPasswordField();
		registerPanel.add(registerConfirmPasswordField);

		JButton button = new JButton("Register");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure we have valid input
				if (registerUsernameField.getText().isEmpty()) {
					JOptionPane
							.showMessageDialog(null,
									"You must provide a username in order to register.");
				} else if (registerPasswordField.getText().isEmpty()) {
					JOptionPane
							.showMessageDialog(null,
									"You must provide a password in order to register.");
				} else if (!registerPasswordField.getText().equals(
						registerConfirmPasswordField.getText())) {
					JOptionPane.showMessageDialog(null,
							"The passwords didn't match.");
				} else {
					// Input has been validated, so send the register packet.
					RegisterPacket packet = new RegisterPacket();
					packet.accountName = registerUsernameField.getText();
					packet.password = registerPasswordField.getText();
					getGameClient().getClient().sendTCP(packet);
				}
			}
		});
		registerPanel.add(button);
		return registerPanel;
	}
	
	/**
	 * Helper function which shows the lobby screen and hides the login window.
	 */
	private void showLobby() {
		getClientWindow().setPanel(new LobbyPanel(getGameClient(), getClientWindow()));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof RegisterResult) {
			RegisterResult result = (RegisterResult) arg;
			// If the registration wasn't successful, show an error message
			if (result != RegisterResult.SUCCESS) {
				JOptionPane.showMessageDialog(null, result);
			} else {
				showLobby();
			}
		} else if (arg instanceof LoginResult) {
			LoginResult result = (LoginResult) arg;
			// If the login wasn't successful, show an error message
			if (result != LoginResult.SUCCESS) {
				JOptionPane.showMessageDialog(null, result);
			} else {
				showLobby();
			}
		}
	}

}
