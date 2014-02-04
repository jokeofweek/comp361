package examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.SwingPropertyChangeSupport;

import com.sun.java.swing.plaf.motif.MotifBorders.BevelBorder;

public class LoginPanel extends JPanel 
{
	private JLabel loginLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 800;

	public LoginPanel()
	{
		build();
	}

	private void build()
	{
		//instantiate all widgets
		loginLabel = new JLabel("Welcome to BattleShips!");
		usernameField = new JTextField();
		usernameLabel = new JLabel("Username");
		passwordField = new JPasswordField();
		passwordLabel = new JLabel("Password");
		loginButton = new JButton("Log In");
		registerButton = new JButton("Register");
		this.setSize(WIDTH, HEIGHT);
		//add everything to the panel
		add(loginLabel);
		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(Box.createVerticalBox());
		add(loginButton);
		add(Box.createVerticalBox());
		add(registerButton);
		//add some style
		setBackground(Color.WHITE);
		for(Component c : this.getComponents())
		{
			c.setFont(new Font("Consolas", Font.PLAIN, 16));
			c.setMaximumSize(new Dimension(WIDTH-40, 30));
			c.setMinimumSize(new Dimension(WIDTH-40, 30));
			c.setPreferredSize(new Dimension(WIDTH-40, 30));
		}
		loginButton.setBackground(new Color(185,225,255));
		loginButton.setPreferredSize(new Dimension(WIDTH-40, 50));
		registerButton.setBackground(new Color(185,225,255));
		loginLabel.setFont(new Font("Consolas", Font.PLAIN, 20));
		loginLabel.setForeground(Color.BLUE);
	}

	public JButton getLoginButton()
	{
		return this.loginButton;
	}
	
	public void setLoginButtonListener(ActionListener listener)
	{
		this.loginButton.addActionListener(listener);
	}
}
