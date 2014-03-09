package comp361.client.ui.setup;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comp361.client.data.GameDescriptorManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.data.GameDescriptor;

public class GameInfoPanel extends JPanel implements Observer {

	private DefaultListModel<String> model;
	private JList<String> playerNameList;
	private GameDescriptorManager manager;
	private int gameDescriptorId;

	public GameInfoPanel(GameDescriptorManager manager, int gameDescriptorId) {
		super(new BorderLayout());
		SwagFactory.style(this);

		this.manager = manager;
		this.gameDescriptorId = gameDescriptorId;

		GameDescriptor desc = manager.getGameDescriptor(gameDescriptorId);
		String name = desc.getName();
		if (desc.isPrivate()) {
			name += " (private)";
		}

		// Put the game name at the top
		JLabel gameNameLabel = new JLabel(name);
		SwagFactory.style(gameNameLabel);
		gameNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(gameNameLabel, BorderLayout.NORTH);

		model = new DefaultListModel<>();
		playerNameList = new JList<>(model);
		refreshData();
		add(playerNameList);
	}

	private void refreshData() {
		// Make sure the game still exists, as this gets called when the player
		// leaves
		// the game descriptor.
		GameDescriptor descriptor = manager.getGameDescriptor(gameDescriptorId);
		if (descriptor == null) {
			return;
		}
		model.clear();
		// Add all players in the game descriptor
		for (String player : descriptor.getPlayers()) {
			if (player != null) {
				if (descriptor.getReadyPlayers().contains(player)) {
					model.addElement(player + "<ready>");
				} else {
					model.addElement(player + " ");
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshData();
	}

}
