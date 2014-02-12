package comp361.client.ui.lobby;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import comp361.client.ui.SwagFactory;

public class InviteOverlayPanel extends JPanel {
	
	public static final int WIDTH = 250;
	public static final int HEIGHT = 400;
	
	public InviteOverlayPanel() {
		Dimension d = new Dimension(WIDTH, HEIGHT);
		setOpaque(false);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = new JButton(Math.random() * 1000 + "");
				SwagFactory.style(b);
				Dimension d = new Dimension(WIDTH, SwagFactory.BUTTON_HEIGHT);
				b.setSize(d);
				b.setMinimumSize(d);
				b.setMaximumSize(d);
				b.setPreferredSize(d);
				add(b);
				revalidate();	
			}
		}).start();
	}
	
}
