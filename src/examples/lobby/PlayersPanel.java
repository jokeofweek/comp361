package examples.lobby;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PlayersPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public PlayersPanel() {
		super(new BorderLayout());
		
		String[] columnNames = {"Online Players", "Games Played", "Games Won"};
		String[][] players = {
				{"Alexander the Great", "9", "8"},
				{"Jorg the Cyborg", "16", "4"}
		};
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(new JLabel("Online Players"), BorderLayout.WEST);
		headerPanel.add(new JTextField("search"), BorderLayout.EAST);
		
		DefaultTableModel tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		JTable playersTable = new JTable(players, columnNames);
		//playersTable.setModel(tableModel);
		
		JPanel playersTablePanel = new JPanel(new BorderLayout());
		playersTablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		playersTablePanel.add(playersTable, BorderLayout.CENTER);
		
		add(headerPanel, BorderLayout.NORTH);
		add(playersTablePanel, BorderLayout.CENTER);
	}
}
