package examples.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class LobbyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public LobbyPanel() {
		setLayout(new BorderLayout());
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		setSize(d);
		setPreferredSize(d);
		
		add(new PlayersPanel(), BorderLayout.CENTER);
		add(new ChatPanel(), BorderLayout.EAST);
	}
}
