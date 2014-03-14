package comp361.client.ui.lobby.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import comp361.client.data.PlayerManager;

public class PlayersTableModel extends AbstractTableModel implements Observer {
	private static final long serialVersionUID = 1L;
	
	private String[] headers;
	private PlayerManager playerManager;
	private List<String> playerNames;
	private Class[] classes = {String.class, Integer.class, Integer.class};
	
	public PlayersTableModel(PlayerManager playerManager, String[] headers) {
		this.headers = headers;
		
		// Update the data
		refreshData(playerManager);
	}

	@Override
	public int getRowCount() {
		return (playerNames == null) ? 0 : playerNames.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return classes[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public String getValueAt(int row, int col) {
		String playerName = playerNames.get(row);
		if (playerManager.getPlayer(playerName) == null) {
			return "";
		}
		if (col == 0) {
			return playerName;
		} else if (col == 1) {
			return "" + playerManager.getPlayer(playerName).getStatistics().getTotalGames();
		} else {
			return "" + playerManager.getPlayer(playerName).getStatistics().getWins();
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	public void refreshData(final PlayerManager playerManager) {
		final PlayersTableModel self = this;
		// We've updated our list of player names, so trigger a refresh of the table
		// data.
		self.playerManager = playerManager;
		// Get the list of player names and sort them
		playerNames = new ArrayList<String>(playerManager.getPlayers());
		Collections.sort(playerNames);
	
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				fireTableDataChanged();
			}
		});
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// If it was the player manager, then refresh data.
		if (o instanceof PlayerManager) {
			refreshData((PlayerManager)o);
		}
	}
}
