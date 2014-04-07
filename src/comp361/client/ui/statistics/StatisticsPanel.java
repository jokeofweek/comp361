package comp361.client.ui.statistics;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import comp361.client.GameClient;
import comp361.client.ui.SwagFactory;
import comp361.shared.data.Player;

public class StatisticsPanel extends JPanel
{
	private GameClient client;
	private JTable aMainTable;
	private StatisticsTableModel aMainPlayerModel;
	
	public StatisticsPanel(GameClient client)
	{
		client = client;
		
		this.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Statistics for: " + client.getPlayerName());
		SwagFactory.style(title);
		
		add(title, BorderLayout.PAGE_START);
		
		aMainPlayerModel = new StatisticsTableModel();
		
		addPlayer(client.getPlayerManager().getPlayer(client.getPlayerName()));

		//Set up the look for the main player table
		aMainTable = new JTable(aMainPlayerModel);
		aMainTable.getTableHeader().setFont(SwagFactory.FONT);
		aMainTable.setFont(SwagFactory.FONT);
        aMainTable.setFillsViewportHeight(true);
        aMainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Create the scroll pane and add the table to it.
        JScrollPane mainScrollPane = new JScrollPane(aMainTable);
 
        //Add the scroll pane to this panel.
        add(mainScrollPane, BorderLayout.CENTER);
        
        SwagFactory.style(this);
	}
	
	/**
	 * @param pPlayer player to add to model
	 * Utility method to add statistics.
	 */
	public void addPlayer(Player pPlayer)
	{
		int i = 0;
		for(String string : pPlayer.getStatistics().getStatistics().keySet())
		{
			aMainPlayerModel.addNewData(string, pPlayer.getStatistics().getStatistics().values().toArray()[i]);
			i++;
		}
	}
	
}
