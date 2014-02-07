package examples.lobby;

import javax.swing.table.AbstractTableModel;

public class PlayersTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	public String[][] playerInfo;
	
	public PlayersTableModel(String[][] playerInfo) {
		this.playerInfo = playerInfo;
	}

	@Override
	public int getRowCount() {
		return playerInfo.length;
	}

	@Override
	public int getColumnCount() {
		if (playerInfo.length > 0) {
			return playerInfo[0].length;
		} else {
			return 0;
		}
	}

	@Override
	public String getValueAt(int row, int col) {
		return playerInfo[row][col];
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
