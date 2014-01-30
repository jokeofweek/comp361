package statisticsUI;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class StatisticsExampleFrame extends JFrame
{
	private JPanel container;
	
	public StatisticsExampleFrame() throws IOException 
	{
		container = new JPanel(new BorderLayout());
		
		container.add(new StatisticsPanel(), BorderLayout.CENTER);
		
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
