package comp361.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import comp361.client.GameClient;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.LoginResult;
import comp361.shared.packets.server.RegisterResult;

public class LoginPanel extends ClientPanel {

	private static final int LOGIN_PANEL_WIDTH = 400;
	private static final int LOGIN_PANEL_HEIGHT = 600;

	private JLabel loginLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;

	public LoginPanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		build();
	}

	private void build() {
		JPanel container = new JPanel(new BorderLayout());
		SwagFactory.style(container);

		// Add the logo
		JLabel logoLabel = new JLabel(new ImageIcon(SwagFactory.LOGO_IMAGE));
		Dimension logoSize = new Dimension(SwagFactory.LOGO_IMAGE.getWidth(),
				SwagFactory.LOGO_IMAGE.getHeight());
		logoLabel.setSize(logoSize);
		logoLabel.setMinimumSize(logoSize);
		logoLabel.setMaximumSize(logoSize);
		container.add(logoLabel, BorderLayout.NORTH);

		container.add(Box.createHorizontalStrut(LOGIN_PANEL_WIDTH / 4),
				BorderLayout.EAST);
		container.add(Box.createHorizontalStrut(LOGIN_PANEL_WIDTH / 4),
				BorderLayout.WEST);

		// Create the actual menu panel
		JPanel loginPanel = new JPanel();
		SwagFactory.style(loginPanel);
		loginPanel.setSize(LOGIN_PANEL_WIDTH, LOGIN_PANEL_HEIGHT);

		// instantiate all widgets
		loginLabel = new JLabel("Welcome to BattleShips!");
		usernameField = new JTextField();
		usernameLabel = new JLabel("Username");
		passwordField = new JPasswordField();
		passwordLabel = new JLabel("Password");
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginActionListener());
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(new RegisterActionListener());

		// add everything to the panel
		loginPanel.add(loginLabel);
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField);
		loginPanel.add(Box.createVerticalBox());
		loginPanel.add(loginButton);
		loginPanel.add(Box.createVerticalBox());
		loginPanel.add(registerButton);

		// add some style
		setBackground(Color.WHITE);
		for (Component c : loginPanel.getComponents()) {
			SwagFactory.style((JComponent) c);
			c.setMaximumSize(new Dimension(LOGIN_PANEL_WIDTH - 40, SwagFactory.BUTTON_HEIGHT));
			c.setMinimumSize(new Dimension(LOGIN_PANEL_WIDTH - 40, SwagFactory.BUTTON_HEIGHT));
			c.setPreferredSize(new Dimension(LOGIN_PANEL_WIDTH - 40, SwagFactory.BUTTON_HEIGHT));
		}
		SwagFactory.style(loginButton);
		SwagFactory.style(registerButton);
		loginLabel.setFont(SwagFactory.FONT.deriveFont(20));
		loginLabel.setForeground(Color.BLUE);

		container.add(loginPanel, BorderLayout.CENTER);

		add(container);
	}
	
	@Override
	public JComponent getStartingFocus() {
		return usernameField;
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
	
	private class LoginActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Make sure we have valid input
			if (usernameField.getText().isEmpty()) {
				JOptionPane
						.showMessageDialog(null,
								"You must provide a username in order to login.");
			} else if (passwordField.getPassword().length == 0) {
				JOptionPane
						.showMessageDialog(null,
								"You must provide a password in order to login.");
			} else {
				// Input has been validated, so send the login packet.
				LoginPacket packet = new LoginPacket();
				packet.accountName = usernameField.getText();
				packet.password = new String(passwordField.getPassword());
				getGameClient().getClient().sendTCP(packet);
			}
		}
	}
	
	private class RegisterActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Make sure we have valid input
			if (usernameField.getText().isEmpty()) {
				JOptionPane
						.showMessageDialog(null,
								"You must provide a username in order to register.");
			} else if (passwordField.getPassword().length == 0) {
				JOptionPane
						.showMessageDialog(null,
								"You must provide a password in order to register.");
			} else {
				// Input has been validated, so send the register packet.
				RegisterPacket packet = new RegisterPacket();
				packet.accountName = usernameField.getText();
				packet.password = new String(passwordField.getPassword());
				getGameClient().getClient().sendTCP(packet);
			}
		}
	}
}
