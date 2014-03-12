package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import comp361.client.GameClient;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.Ship;
import comp361.shared.packets.client.NewGameDescriptorPacket;

public class NewGameDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JLabel gameNameLabel;
	private JLabel gamePasswordLabel;
	private JLabel shipInventoryLabel;
	private JTextField gameNameField;
	private JTextField gamePasswordField;
	private JButton cancelButton;
	private JButton createButton;
	private JComboBox<String> shipInventoryBox;
	
	public NewGameDialog(final ClientWindow frame, final GameClient client) {
		super(frame, true);
		
		JPanel content = new JPanel(new BorderLayout());
		JPanel fields = new JPanel(new GridLayout(6, 1));
		JPanel buttons = new JPanel(new BorderLayout());
		
		gameNameLabel = new JLabel("Name of the game");
		gamePasswordLabel = new JLabel("Password (leave empty for none)");
		gameNameField = new JTextField();
		gamePasswordField = new JTextField();
		shipInventoryLabel = new JLabel("Set of ships");
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(Ship.SHIP_INVENTORY_NAMES);
		shipInventoryBox = new JComboBox<>(model);
		shipInventoryBox.setSelectedIndex(0);
		
		cancelButton = new JButton("Cancel");
		createButton = new JButton("Create Game");
		
		ActionListener handler = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				String name = gameNameField.getText();
				
				if (o != cancelButton) {
					if (name.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Game name cannot be blank");
						return;
					}
					
					NewGameDescriptorPacket packet = new NewGameDescriptorPacket();
					packet.name = name;
					packet.password = gamePasswordField.getText();
					packet.maxPlayers = Constants.NUM_PLAYERS;
					packet.shipInventory = shipInventoryBox.getSelectedIndex();
					client.getClient().sendTCP(packet);
				}
				
				NewGameDialog.this.dispose();
			}
		};
		
		createButton.addActionListener(handler);
		cancelButton.addActionListener(handler);
		gameNameField.addActionListener(handler);
		gamePasswordField.addActionListener(handler);
		
		fields.add(gameNameLabel);
		fields.add(gameNameField);
		fields.add(gamePasswordLabel);
		fields.add(gamePasswordField);
		fields.add(shipInventoryLabel);
		fields.add(shipInventoryBox);
		
		buttons.add(cancelButton, BorderLayout.WEST);
		buttons.add(createButton, BorderLayout.EAST);
		buttons.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		content.add(fields, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.SOUTH);
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		SwagFactory.style(content);
		
		// SwagFactory breaks these buttons for some reason
		// Undo preferred size on buttons
		cancelButton.setPreferredSize(null);
		createButton.setPreferredSize(null);
		
		Dimension d = new Dimension(400, 200);
		setPreferredSize(d);
		setSize(d);
		setContentPane(content);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
