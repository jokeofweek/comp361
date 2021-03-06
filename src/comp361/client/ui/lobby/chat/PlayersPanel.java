package comp361.client.ui.lobby.chat;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableRowSorter;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.lobby.LobbyPanel;

public class PlayersPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private PlayersTableModel tableModel;
	
	public PlayersPanel(GameClient gameClient) {
		super(new BorderLayout());
		SwagFactory.style(this);
		setBorder(BorderFactory.createEmptyBorder(LobbyPanel.COMPONENT_SPACING, LobbyPanel.COMPONENT_SPACING, 
				0, 0));
		
		String[] columnNames = {"Name", "Games", "Wins"};
		String[][] players = {
				{"Alexander the Great", "9", "8"},
				{"Jorg the Cyborg", "16", "4"}
		};
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		SwagFactory.style(headerPanel);
		
		JLabel onlinePlayersLabel = new JLabel("Online Players");
		onlinePlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		SwagFactory.style(onlinePlayersLabel);
		headerPanel.add(onlinePlayersLabel, BorderLayout.CENTER);

		//headerPanel.add(new JTextField("search"), BorderLayout.EAST);
		
		JTable playersTable = new JTable(players, columnNames);
		tableModel = new PlayersTableModel(gameClient.getPlayerManager(), columnNames);
		playersTable.setModel(tableModel);
		playersTable.setRowSorter(new TableRowSorter<PlayersTableModel>(tableModel));
		
		JPanel playersTablePanel = new JPanel(new BorderLayout());
		playersTablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		playersTablePanel.add(playersTable.getTableHeader(), BorderLayout.NORTH);
		playersTablePanel.add(playersTable, BorderLayout.CENTER);
		
		add(headerPanel, BorderLayout.NORTH);
		add(playersTablePanel, BorderLayout.CENTER);
	}
	
	public PlayersTableModel getTableModel() {
		return tableModel;
	}
}
