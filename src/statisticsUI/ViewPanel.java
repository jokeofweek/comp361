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
	DefaultComboBoxModel<String> model;
	
	/**
	 * @param main the owner
	 * @param players the player manager responsible for managing the players around
	 * 
	 */
	public ViewPanel(Player main, PlayerManager players)
	{
		//Set the layout
		this.setLayout(new BorderLayout());
		
		//Make the combo box and its base array
		JPanel comboBoxPane = new JPanel();
		String[] comboBoxItems = new String[players.getPlayers().size()];
		
		//Fill the base array
		int i = 0;
		for(String string : players.getPlayers())
		{
			comboBoxItems[i] = string;
			i++;
		}
		
		//Make the model
		model = new DefaultComboBoxModel<String>(comboBoxItems);
		JComboBox<String> cb = new JComboBox<String>(model);
		
		//To some jazz
		cb.setEditable(false);
		cb.addItemListener(this);
		comboBoxPane.add(cb);
		
		//Make the main player panel
		aMain = new StatisticsPanel(main);
		
		//Make the cards
		aCards = new JPanel();
		
		//Set the card layout
		aCards = new JPanel(new CardLayout());
		
		//Fill the combobox
		for(String string : comboBoxItems)
		{
			StatisticsPanel panel = new StatisticsPanel(players.getMap().get(string));
			aCards.add(panel, string);
			
		}
		
		//Add the main panel
		add(aMain, BorderLayout.WEST);
		
		//Add the cards
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
		//Get the manager and cast it
		PlayerManager manager = (PlayerManager) o;
		String[] comboBoxItems = new String[manager.getPlayers().size()];
		
		//Clear the model
		model.removeAllElements();
		
		//Fill the model with the new elements
		int i = 0;
		for(String string : manager.getPlayers())
		{
			comboBoxItems[i] = string;
			i++;
			model.addElement(string);
			
			StatisticsPanel panel = new StatisticsPanel(manager.getMap().get(string));
			aCards.add(panel, string);
		}
		

	}
}
