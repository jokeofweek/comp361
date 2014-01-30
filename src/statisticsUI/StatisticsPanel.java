package statisticsUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class StatisticsPanel extends JPanel
{
	private JTable aTable;
	private StatisticsTableModel aModel;
	
	public StatisticsPanel()
	{
		this.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Statistics");
		title.setFont(new Font("Times", Font.BOLD, 18));
		
		add(title, BorderLayout.PAGE_START);
		
		aModel = new StatisticsTableModel();
		
		Statistics statistic = new Statistics();
		statistic.initialiseStatisticExample();
		
		int i = 0;
		for(String string : statistic.getStatistics().keySet())
		{
			aModel.addNewData(string, statistic.getStatistics().values().toArray()[i]);
			i++;
		}

		aTable = new JTable(aModel);
		aTable.getTableHeader().setFont(new Font("Times", Font.ITALIC, 16));
//		aTable.setUI(new NimbusLookAndFeel());
		aTable.setFont(new Font("Times", Font.PLAIN, 14));
        aTable.setPreferredScrollableViewportSize(new Dimension(800, 600));
        aTable.setFillsViewportHeight(true);
        
        aTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(aTable);
 
        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.WEST);
	}
	
}
