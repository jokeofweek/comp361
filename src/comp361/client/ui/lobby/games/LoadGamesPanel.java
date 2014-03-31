package comp361.client.ui.lobby.games;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import comp361.client.GameClient;

public class LoadGamesPanel extends JPanel {

	private LoadGamesTableModel tableModel;

	public LoadGamesPanel(GameClient gameClient) {
		super(new BorderLayout());

		JPanel container = new JPanel(new BorderLayout());

		tableModel = new LoadGamesTableModel();
		LoadGamesTable table = new LoadGamesTable(gameClient, tableModel);


		JScrollPane scrollPane = new JScrollPane(table);

		container.add(scrollPane);

		add(container);
	}

	public LoadGamesTableModel getTableModel() {
		return tableModel;
	}

}
