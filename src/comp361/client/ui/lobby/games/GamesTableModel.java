package comp361.client.ui.lobby.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import comp361.client.data.GameDescriptorManager;
import comp361.shared.data.GameDescriptor;

public class GamesTableModel extends AbstractTableModel implements Observer {

	public static final int JOIN_COLUMN = 3;
	private static final String[] headers =  {"Name", "Players", "Private?", "Join"};
	
	private GameDescriptorManager manager;
	/**
	 * This is a list of the descriptors IDs.
	 */
	public static List<Integer> descriptors;
	
	public GamesTableModel(GameDescriptorManager manager) {
		this.manager = manager;
		
		refreshData(manager);
	}
	
	@Override
	public int getRowCount() {
		return descriptors.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
		
	}
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}
	
	public String[] getHeaders() {
		return headers;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col == JOIN_COLUMN;
	}
	
	public GameDescriptor getGameDescriptor(int row) {
		return manager.getGameDescriptor(descriptors.get(row));
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (descriptors.size() == 0) {
			return null;
		}
		GameDescriptor descriptor = manager.getGameDescriptor(descriptors.get(rowIndex));
		if (columnIndex == 0) {
			return descriptor.getName();
		} else if (columnIndex == 1) {
			// Caclulate number of players
			String[] players = descriptor.getPlayers();
			int count = 0;
			for (int i = 0; i < players.length; i++) {
				if (players[i] != null) {
					count++;
				}
			}
			return count + " / " + descriptor.getMaxPlayers();
		} else if (columnIndex == 2) {
			if (descriptor.getPassword() != null && !descriptor.getPassword().isEmpty()) {
				return "\u2713";
			} else {
				return "";
			}
		} else if (columnIndex == 3) {
			
			
			return (descriptor.isStarted() ? "Observe" : "Join");
			
			
		}
		
		throw new IndexOutOfBoundsException();
	}

	public void refreshData(GameDescriptorManager manager) {
		this.manager = manager;

		// Create a list of the descriptors IDs and sort them
		descriptors = new ArrayList<>(manager.getGameDescriptorIds());
		Collections.sort(descriptors);
		// Remove full games
		/*for (int i = descriptors.size() - 1; i >= 0; i--) {
			GameDescriptor d = manager.getGameDescriptor(descriptors.get(i));
			if (d.isStarted() || (d.getMaxPlayers() - d.getPlayerCount() == 0)) {
				descriptors.remove(i);
			}
		}*/
		// We've updated our list, so trigger a refresh of the table
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
		if (o instanceof GameDescriptorManager) {
			refreshData((GameDescriptorManager)o);
		}
	}

}
