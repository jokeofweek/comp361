package comp361.client.ui.setup;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import comp361.client.ui.SwagFactory;
import comp361.client.data.GameDescriptorManager;

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
		
		// Put the game name at the top
		JLabel gameNameLabel = new JLabel(manager.getGameDescriptor(gameDescriptorId).getName());
		SwagFactory.style(gameNameLabel);
		gameNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(gameNameLabel, BorderLayout.NORTH);
		
		model = new DefaultListModel<>();
		playerNameList = new JList<>(model);
		refreshData();
		add(playerNameList);
	}
	
	private void refreshData() {
		model.clear();
		// Aadd all players in the game descriptor
		for (String player : manager.getGameDescriptor(gameDescriptorId).getPlayers()) {
			model.addElement(player);
		}
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		refreshData();
	}

}
