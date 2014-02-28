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

	public String[] headers  = {"Name", "Private?"};
	
	private GameDescriptorManager manager;
	/**
	 * This is a list of the descriptors IDs.
	 */
	public static List<Integer> descriptors;
	
	public GamesTableModel(GameDescriptorManager manager, String[] headers) {
		this.manager = manager;
		this.headers = headers;
		
		refreshData(manager);
	}
	
	@Override
	public int getRowCount() {
		return descriptors.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		GameDescriptor descriptor = manager.getGameDescriptor(descriptors.get(rowIndex));
		if (columnIndex == 0) {
			return descriptor.getName();
		} else {
			if (descriptor.getPassword() != null) {
				return "✓";
			} else {
				return "";
			}
		}
	}

	private void refreshData(GameDescriptorManager manager) {
		this.manager = manager;
		// Create a list of the descriptors IDs and sort them
		descriptors = new ArrayList<>(manager.getGameDescriptorIds());
		Collections.sort(descriptors);
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
