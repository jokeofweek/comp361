package comp361.client.ui.lobby.games;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import comp361.client.GameClient;

public class GamesPanel extends JPanel {

	private GamesTableModel tableModel;
	
	public GamesPanel(GameClient gameClient) {
		super(new BorderLayout());
		
		JPanel container = new JPanel(new BorderLayout());
		
		String[] headers  = {"Name", "Private?"};
				
		JTable gamesTable = new JTable(null, headers);
		tableModel = new GamesTableModel(gameClient.getGameDescriptorManager(), headers);
		gamesTable.setModel(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(gamesTable);

		container.add(scrollPane);
		
		add(container);
	}
	
	public GamesTableModel getTableModel() {
		return tableModel;
	}
	
}
