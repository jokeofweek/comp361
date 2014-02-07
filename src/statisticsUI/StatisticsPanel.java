package statisticsUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import comp361.client.data.Player;
import comp361.shared.data.Statistics;

public class StatisticsPanel extends JPanel
{
	private Player aMain;
	private JTable aMainTable;
	private StatisticsTableModel aMainPlayerModel;
	
	public StatisticsPanel(Player pMain)
	{
		aMain = pMain;
		
		
		this.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Statistics for: " + aMain.getName());
		title.setFont(new Font("Times", Font.BOLD, 18));
		
		add(title, BorderLayout.PAGE_START);
		
		aMainPlayerModel = new StatisticsTableModel();
		
		int i = 0;
		for(String string : aMain.getStatistics().getStatistics().keySet())
		{
			aMainPlayerModel.addNewData(string, aMain.getStatistics().getStatistics().values().toArray()[i]);
			i++;
		}
		

		//Set up the look for the main player table
		aMainTable = new JTable(aMainPlayerModel);
		aMainTable.getTableHeader().setFont(new Font("Times", Font.ITALIC, 16));
		aMainTable.setFont(new Font("Times", Font.PLAIN, 14));
//        aMainTable.setPreferredScrollableViewportSize(new Dimension(800, 600));
        aMainTable.setFillsViewportHeight(true);
        aMainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Set up the look for the other player table
//        aOtherTable = new JTable(aOtherPlayerModel);
//        aOtherTable.getTableHeader().setFont(new Font("Times", Font.ITALIC, 16));
//        aOtherTable.setFont(new Font("Times", Font.PLAIN, 14));
//        aOtherTable.setPreferredScrollableViewportSize(new Dimension(800, 600));
//        aOtherTable.setFillsViewportHeight(true);
//        aOtherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Create the scroll pane and add the table to it.
        JScrollPane mainScrollPane = new JScrollPane(aMainTable);
 
        //Add the scroll pane to this panel.
        add(mainScrollPane, BorderLayout.CENTER);

	}
	
}
