package statisticsUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.data.PlayerManager;
import comp361.shared.data.Player;

public class StatisticsExampleFrame extends JFrame
{
	private JPanel container;
	
	public StatisticsExampleFrame() throws IOException 
	{
		container = new JPanel(new BorderLayout());
		
		Player bob = new Player("Bob");
		bob.getStatistics().initialiseStatisticExample();
		
		Player rob = new Player("Rob");
		rob.getStatistics().initialiseOtherStatistics();
		
		final PlayerManager players = new PlayerManager();
		players.addPlayer(bob);
		players.addPlayer(rob);
		
		ViewPanel panel = new ViewPanel(bob, players);
		players.addObserver(panel);
		
		container.add(panel, BorderLayout.CENTER);
		
		JButton addPlayer = new JButton("Add Player");
		addPlayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Player Marc = new Player("Marc");
				Marc.getStatistics().initialiseEvenOtherStatistics();
				
				players.addPlayer(Marc);
				
			}
		});
		
		add(addPlayer, BorderLayout.BEFORE_FIRST_LINE);
		
		add(container);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try 
				{	
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					new StatisticsExampleFrame();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	

}
