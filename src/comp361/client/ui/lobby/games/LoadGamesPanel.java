package comp361.client.ui.lobby.games;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import comp361.client.GameClient;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.lobby.LobbyPanel;

public class LoadGamesPanel extends JPanel {

	private LoadGamesTableModel tableModel;

	public LoadGamesPanel(GameClient gameClient, ClientWindow window) {
		super(new BorderLayout());

		JPanel container = new JPanel(new BorderLayout());

		tableModel = new LoadGamesTableModel(gameClient);
		LoadGamesTable table = new LoadGamesTable(gameClient, window, tableModel);


		JScrollPane scrollPane = new JScrollPane(table);

		container.add(scrollPane);

		add(container);
	}

	public LoadGamesTableModel getTableModel() {
		return tableModel;
	}

}
