package statisticsUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import comp361.client.data.Player;
import comp361.client.data.PlayerManager;

public class ViewPanel extends JPanel implements ItemListener
{
	JPanel aCards;
	StatisticsPanel aMain;
	
	public ViewPanel(Player main, PlayerManager players)
	{
		this.setLayout(new BorderLayout());
		
		JPanel comboBoxPane = new JPanel();
		String[] comboBoxItems = new String[players.getPlayers().size()];
		
		int i = 0;
		for(String string : players.getPlayers())
		{
			comboBoxItems[i] = string;
			i++;
		}
		
		JComboBox cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener(this);
		comboBoxPane.add(cb);
		
		aMain = new StatisticsPanel(main);
		
		aCards = new JPanel();
		
		aCards = new JPanel(new CardLayout());
		
		for(String string : comboBoxItems)
		{
			StatisticsPanel panel = new StatisticsPanel(players.getMap().get(string));
			aCards.add(panel, string);
			
		}
		
//		add(comboBoxPane, BorderLayout.BEFORE_FIRST_LINE);
		add(aMain, BorderLayout.WEST);
		
		JPanel temp = new JPanel(new BorderLayout());
		temp.add(comboBoxPane, BorderLayout.EAST);
		temp.add(aCards, BorderLayout.CENTER);
		
		add(temp, BorderLayout.EAST);
//		add(aCards, BorderLayout.EAST);

	}

	
	@Override
	public void itemStateChanged(ItemEvent pEvent) 
	{
		CardLayout cl = (CardLayout) (aCards.getLayout());
		cl.show(aCards, (String) pEvent.getItem());		
	}
}
