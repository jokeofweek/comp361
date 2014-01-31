package examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainMenu extends JFrame 
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private JPanel parentPanel;
	private JPanel loginPanel;
	private JPanel registrationPanel;
	private JPanel menuPanel;
	private JLabel loginLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JTextField passwordField;
	private JButton loginButton;
	private JButton logoutButton;
	private ImageIcon logo;
	private final ButtonListener BUTTON_LISTENER = new ButtonListener();
	
	public static void main(String[] args)
	{
		new MainMenu().setVisible(true);
	}
	
	public MainMenu()
	{
		this.parentPanel = new JPanel();
		importResources();
		setAttributes();
		buildUI();
		this.add(parentPanel);
	}
	
	private void buildUI()
	{
		buildLoginPanel();
		buildMainMenu();
		buildRegistrationMenu();
	}
	
	private void setAttributes()
	{
		setSize(WIDTH, HEIGHT);
		parentPanel.setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Battleships");
	}
	
	private void importResources()
	{
		logo = new ImageIcon("..\\..\\images\\logo.png");
	}
	
	private void buildLoginPanel()
	{
		//instantiate all widgets
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
		loginLabel = new JLabel("Welcome to BattleShips!");
		usernameField = new JTextField();
		usernameLabel = new JLabel("Username");
		passwordField = new JTextField();
		passwordLabel = new JLabel("Password");
		loginButton = new JButton("Log In");
		loginPanel.setBorder(BorderFactory.createEtchedBorder());
		loginPanel.setSize(WIDTH/2, (int)(HEIGHT/1.5));
		//put some style in there
		//add action listener to button
		loginButton.addActionListener(BUTTON_LISTENER);
		//finally, add everything to the panel
		loginPanel.add(loginLabel);
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField);
		loginPanel.add(loginButton);
		parentPanel.add(loginPanel, BorderLayout.CENTER);
	}
	
	private void buildMainMenu()
	{
		this.menuPanel = new JPanel();
		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(BUTTON_LISTENER);
		menuPanel.add(new JLabel("This is the menu."), BorderLayout.CENTER);
		menuPanel.add(logoutButton, BorderLayout.CENTER);
	}
	
	private void buildRegistrationMenu()
	{
		this.registrationPanel = new JPanel();
	}
	
	private class ButtonListener implements ActionListener
	{
		//switches the view to the main menu
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == loginButton)
			{
				parentPanel.remove(loginPanel);
				parentPanel.add(menuPanel);
			}
			if(e.getSource() == logoutButton)
			{
				parentPanel.remove(menuPanel);
				parentPanel.add(loginPanel);
			}
			parentPanel.validate();
			parentPanel.repaint();
		}
	}
}
