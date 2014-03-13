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
import comp361.shared.data.Ship;

public class GamesTableModel extends AbstractTableModel implements Observer {

	public static final int JOIN_COLUMN = 4;
	private static final String[] headers =  {"Name", "Players", "Private?", "Ship Set", "Join"};
	
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
			return Ship.SHIP_INVENTORY_NAMES[descriptor.getShipInventory()];
		} else if (columnIndex == 4) {
			return (descriptor.isStarted() ? "Observe" : "Join");
		}
		
		throw new IndexOutOfBoundsException();
	}

	public void refreshData(final GameDescriptorManager manager) {
		this.manager = manager;

		// We've updated our list, so trigger a refresh of the table
		// data.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				// Create a list of the descriptors IDs and sort them
				descriptors = new ArrayList<>(manager.getGameDescriptorIds());
				Collections.sort(descriptors);
				// Remove full games
				for (int i = descriptors.size() - 1; i >= 0; i--) {
					GameDescriptor d = manager.getGameDescriptor(descriptors.get(i));
					if (d.isStarted() || (d.getMaxPlayers() - d.getPlayerCount() == 0)) {
						descriptors.remove(i);
					}
				}
				
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
