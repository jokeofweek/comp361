package comp361.client.ui.lobby.games;

import javax.swing.table.AbstractTableModel;

public class GamesTableModel extends AbstractTableModel {

	public String[] headers  = {"Name", "Private?"};
	
	public static Object[][] values = {
		{"Mo's Palace", true},
		{"Dom's Game", false}
	};
	
	public GamesTableModel(Object[][] values, String[] headers) {
		this.values = values;
		this.headers = headers;
	}
	
	@Override
	public int getRowCount() {
		return values.length;
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
		if (columnIndex == 0) {
			return values[rowIndex][0];
		} else {
			if ((Boolean)values[rowIndex][1]) {
				return "✓";
			} else {
				return "";
			}
		}
	}

}
