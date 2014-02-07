package examples.lobby;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LobbyFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public LobbyFrame() {
		add(new LobbyPanel());
		setBackground(Color.WHITE);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LobbyFrame();
			}
		});
	}
}
