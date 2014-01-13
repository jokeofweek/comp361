package comp361.client;

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

import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.RegisterResult;

public class LoginWindow extends ClientJFrame {

	private static final long serialVersionUID = 7525404399381734980L;

	private JTextField registerUsernameField;
	private JTextField registerPasswordField;	

	public LoginWindow(GameClient gameClient) {
		super(gameClient);
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(getLoginPanel());
		mainPanel.add(getRegisterPanel());
		add(mainPanel);

		setTitle("Battleships");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private JPanel getLoginPanel() {
		return new JPanel();
	}
	
	private JPanel getRegisterPanel() {
		JPanel registerPanel = new JPanel(new GridLayout(0, 1));
		
		registerPanel.add(new JLabel("Username:"));
		registerUsernameField = new JTextField();
		registerPanel.add(registerUsernameField);
		
		registerPanel.add(new JLabel("Password:"));
		registerPasswordField = new JPasswordField();
		registerPanel.add(registerPasswordField);		
		
		JButton test = new JButton("Register");
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure we have valid input
				if (registerUsernameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must provide a username in order to register.");
				} else if (registerPasswordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must provide a password in order to register.");
				} else {
					// Input has been validated, so send the register packet.
					RegisterPacket packet = new RegisterPacket();
					packet.accountName = registerUsernameField.getText();
					packet.password = registerPasswordField.getText();
					getGameClient().getClient().sendTCP(packet);
				}				
			}
		});
		registerPanel.add(test);
		return registerPanel;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof RegisterResult) {			
			RegisterResult result = (RegisterResult) arg;
			// If the registration wasn't successful, show an error message
			if (result != RegisterResult.SUCCESS) {
				JOptionPane.showMessageDialog(null, result);
			} else {
				// We registered! Move on to lobby
				final LoginWindow self = this;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new LobbyWindow(getGameClient()).setVisible(true);
						self.setVisible(false);
					}
				});
			}
		}	
	}
	
}
