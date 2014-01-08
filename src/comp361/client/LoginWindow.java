package comp361.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.RegisterResult;

public class LoginWindow extends ClientJFrame {

	private static final long serialVersionUID = 7525404399381734980L;


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
		JPanel registerPanel = new JPanel();
		JButton test = new JButton("Test Register");
		final JFrame self = this;
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterPacket packet = new RegisterPacket();
				packet.accountName = "dominic";
				packet.password = "charley";
				getGameClient().getClient().sendTCP(packet);				
			}
		});
		registerPanel.add(test);
		return registerPanel;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof RegisterResult) {
			
			RegisterResult result = (RegisterResult) arg;
			// If the registration wasn't succesful, show an error message
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
		JOptionPane.showMessageDialog(null, arg);	
	}
	
}
