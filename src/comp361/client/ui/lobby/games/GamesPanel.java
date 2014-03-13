package comp361.client.ui.lobby.games;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import comp361.client.GameClient;

public class GamesPanel extends JPanel {

	private GamesTableModel tableModel;
	
	public GamesPanel(GameClient gameClient) {
		super(new BorderLayout());
		
		JPanel container = new JPanel(new BorderLayout());
		
		tableModel = new GamesTableModel(gameClient.getGameDescriptorManager());
		GamesTable table = new GamesTable(gameClient, tableModel);
		
		
		JScrollPane scrollPane = new JScrollPane(table);

		container.add(scrollPane);
		
		add(container);
	}
	
	public GamesTableModel getTableModel() {
		return tableModel;
	}
	
}
