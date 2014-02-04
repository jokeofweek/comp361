package examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainUI extends JFrame 
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private LoginPanel loginPanel;
	private JPanel registrationPanel;
	private MenuPanel menuPanel;
	private JButton logoutButton;
	private ImageIcon logo;
	private final LoginListener LOGIN_BUTTON_LISTENER = new LoginListener();
	private final MenuListener MENU_BUTTON_LISTENER = new MenuListener();
	
	public static void main(String[] args)
	{
		new MainUI().setVisible(true);
	}
	
	public MainUI()
	{
		importResources();
		setAttributes();
		buildUI();
	}
	
	private void buildUI()
	{
		add(new JLabel(logo), BorderLayout.PAGE_START);
		getContentPane().setBackground(Color.WHITE);
		add(Box.createHorizontalStrut(WIDTH/4), BorderLayout.EAST);
		add(Box.createHorizontalStrut(WIDTH/4), BorderLayout.WEST);
		setBackground(Color.WHITE);
		buildLoginPanel();
		buildMainMenu();
		buildRegistrationMenu();
	}
	
	private void setAttributes()
	{
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Battleships");
	}
	
	private void importResources()
	{
		logo = new ImageIcon("C:\\Hacking\\Projects\\comp361\\images\\logo.png");
	}
	
	private void buildLoginPanel()
	{
		loginPanel = new LoginPanel();
		loginPanel.setLoginButtonListener(LOGIN_BUTTON_LISTENER);
		add(loginPanel, BorderLayout.CENTER);
	}
	
	private void buildMainMenu()
	{
		menuPanel = new MenuPanel();
		menuPanel.setButtonListener(MENU_BUTTON_LISTENER);
	}
	
	private void buildRegistrationMenu()
	{
		registrationPanel = new JPanel();
		//instantiate all components
		//add action listeners to buttons
		//add everything to the panel
		//put some style in there
	}
	
	private class LoginListener implements ActionListener
	{
		//switches the view to the main menu
		public void actionPerformed(ActionEvent e)
		{
			remove(loginPanel);
			add(menuPanel);
			validate();
			repaint();
		}
	}
	
	private class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == menuPanel.getLogoutButton())
			{
				remove(menuPanel);
				add(loginPanel);
			}
			validate();
			repaint();
		}
	}
}
