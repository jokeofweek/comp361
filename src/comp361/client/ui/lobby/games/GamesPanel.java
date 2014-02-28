package comp361.client.ui.lobby.games;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GamesPanel extends JPanel {

	public GamesPanel() {
		super(new BorderLayout());
		
		setup();
	}
	
	private void setup() {
		JPanel container = new JPanel(new BorderLayout());
		
		String[] headers  = {"Name", "Private?"};
		Object[][] values = {
			{"Mo's Palace", true},
			{"Dom's Game", false}
		};
		
		
		JTable gamesTable = new JTable(values, headers);
		GamesTableModel tableModel = new GamesTableModel(values, headers);
		gamesTable.setModel(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(gamesTable);

		container.add(scrollPane);
		
		add(container);
	}
	
}
