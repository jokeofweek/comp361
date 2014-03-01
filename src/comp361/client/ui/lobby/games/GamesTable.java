package comp361.client.ui.lobby.games;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import comp361.client.ui.SwagFactory;
import comp361.client.ui.util.ButtonColumn;

public class GamesTable extends JTable {

	private GamesTableModel model;
	private static final int ROW_HEIGHT = SwagFactory.BUTTON_HEIGHT;
	
	public GamesTable(GamesTableModel model) {
		super(null, model.getHeaders());
		setModel(model);
		
		// Set the table styling
		setRowHeight(ROW_HEIGHT);
		
		// Create the button column
		new ButtonColumn(this, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "ABC");
			}
		}, GamesTableModel.JOIN_COLUMN);
	}
	
}
