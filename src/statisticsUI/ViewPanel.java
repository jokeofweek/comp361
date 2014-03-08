package statisticsUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import comp361.client.data.PlayerManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.data.Player;

public class ViewPanel extends JPanel implements ItemListener, Observer
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
		
		final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
		JComboBox cb = new JComboBox(model);
		
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
		
		add(aMain, BorderLayout.WEST);
		
		JPanel temp = new JPanel(new BorderLayout());
		SwagFactory.style(temp);
		temp.add(comboBoxPane, BorderLayout.AFTER_LAST_LINE);
		temp.add(aCards, BorderLayout.CENTER);
		
		add(temp, BorderLayout.EAST);
		
		SwagFactory.style(this);

	}

	
	@Override
	public void itemStateChanged(ItemEvent pEvent) 
	{
		CardLayout cl = (CardLayout) (aCards.getLayout());
		cl.show(aCards, (String) pEvent.getItem());		
	}


	@Override
	public void update(Observable o, Object arg) 
	{
		
	}
}
