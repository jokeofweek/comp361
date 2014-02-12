package statisticsUI;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
		
		PlayerManager players = new PlayerManager();
		players.addPlayer(bob);
		players.addPlayer(rob);
		
		container.add(new ViewPanel(bob, players), BorderLayout.CENTER);
		
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
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
