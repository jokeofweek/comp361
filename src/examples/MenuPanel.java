package examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel 
{
	private JLabel menuLabel;
	private JButton lobbyButton;
	private JButton statButton;
	private JButton configButton;
	private JButton logoutButton;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 800;
	
	public MenuPanel()
	{
		build();
	}
	
	private void build()
	{
		setSize(new Dimension(WIDTH, HEIGHT));
		//instantiate components
		menuLabel = new JLabel("Main Menu");
		lobbyButton = new JButton("Lobby");
		statButton = new JButton("Statistics");
		configButton = new JButton("Settings");
		logoutButton = new JButton("Logout");
		//add everything to the panel
		add(menuLabel);
		add(Box.createVerticalBox());
		add(lobbyButton);
		add(Box.createVerticalBox());
		add(statButton);
		add(Box.createVerticalBox());
		add(configButton);
		add(Box.createVerticalBox());
		add(logoutButton);
		//add some style
		setBackground(Color.WHITE);
		for(Component c : this.getComponents())
		{
			if(c instanceof JButton)
			{
				c.setMaximumSize(new Dimension(WIDTH-40, 40));
				c.setMinimumSize(new Dimension(WIDTH-40, 40));
				c.setPreferredSize(new Dimension(WIDTH-40, 40));
				c.setBackground(new Color(185,225,255));
			}
			else
			{
				c.setMaximumSize(new Dimension(WIDTH-40, 20));
				c.setMinimumSize(new Dimension(WIDTH-40, 20));
				c.setPreferredSize(new Dimension(WIDTH-40, 20));
			}
			c.setFont(new Font("Consolas", Font.PLAIN, 16));
		}
		menuLabel.setFont(new Font("Consolas", Font.PLAIN, 20));
		menuLabel.setForeground(Color.BLUE);
	}
	
	public void setButtonListener(ActionListener listener)
	{
		lobbyButton.addActionListener(listener);
		logoutButton.addActionListener(listener);
		configButton.addActionListener(listener);
		statButton.addActionListener(listener);
	}
	
	public JButton getLobbyButton()
	{
		return this.lobbyButton;
	}
	
	public JButton getStatButton()
	{
		return this.statButton;
	}
	
	public JButton getConfiButton()
	{
		return this.configButton;
	}
	
	public JButton getLogoutButton()
	{
		return this.logoutButton;
	}
}

