package comp361.client.ui.lobby.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import comp361.client.data.GameDescriptorManager;
import comp361.shared.data.GameDescriptor;
import comp361.shared.data.Ship;
import comp361.shared.packets.server.SavedGameContainer;

public class LoadGamesTableModel extends AbstractTableModel implements Observer {

	public static final int JOIN_COLUMN = 3;
	private static final String[] headers =  {"Name", "Opponent", "Ship Set", "Load"};

	/**
	 * This is a list of the descriptors IDs.
	 */
	public static List<SavedGameContainer> containers;

	public LoadGamesTableModel() {
		containers = new ArrayList<SavedGameContainer>();
	}

	@Override
	public int getRowCount() {
		return containers.size();
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
	
	public SavedGameContainer getContainer(int row) {
		return containers.get(row);
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (containers.size() == 0) {
			return null;
		}
		GameDescriptor descriptor = containers.get(rowIndex).descriptor;
		if (columnIndex == 0) {
			return descriptor.getName();
		} else if (columnIndex == 1) {
			for (String player : descriptor.getPlayers()) {
				if (player != null) {
					return player;
				}
			}
			return "";
		} else if (columnIndex == 2) {
			return Ship.SHIP_INVENTORY_NAMES[descriptor.getShipInventory()];
		} else if (columnIndex == 3) {
			return "Load";
		}

		throw new IndexOutOfBoundsException();
	}

	public void refreshData(final List<SavedGameContainer> savedGames) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				containers = savedGames;
				Collections.sort(containers, new Comparator<SavedGameContainer>() {
					@Override
					public int compare(SavedGameContainer o1,
							SavedGameContainer o2) {
						return o1.descriptor.getName().compareTo(o2.descriptor.getName());
					}
				});
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
