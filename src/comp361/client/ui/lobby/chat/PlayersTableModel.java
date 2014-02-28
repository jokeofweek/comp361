package comp361.client.ui.lobby.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;

import comp361.client.data.PlayerManager;

public class PlayersTableModel extends AbstractTableModel implements Observer {
	private static final long serialVersionUID = 1L;
	
	private String[] headers;
	private PlayerManager playerManager;
	private List<String> playerNames;
	
	public PlayersTableModel(PlayerManager playerManager, String[] headers) {
		this.headers = headers;
		
		// Update the data
		refreshData(playerManager);
	}

	@Override
	public int getRowCount() {
		return playerNames.size();
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
		if (col == 0) {
			return playerNames.get(row);
		} else if (col == 1) {
			return Math.floor(Math.random() * 100) + "";
		} else {
			return Math.floor(Math.random() * 100) + "";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	private void refreshData(PlayerManager playerManager) {
		this.playerManager = playerManager;
		// Get the list of player names and sort them
		playerNames = new ArrayList<String>(playerManager.getPlayers());
		Collections.sort(playerNames);
		// We've updated our list of player names, so trigger a refresh of the table
		// data.
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
