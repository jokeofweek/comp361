package comp361.client.ui.lobby;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.Timer;

import comp361.shared.Constants;

public class InviteOverlayPanel extends JPanel {
	
	public static final int WIDTH = 250;
	
	public InviteOverlayPanel() {
		Dimension d = new Dimension(WIDTH, Constants.SCREEN_HEIGHT);
		setOpaque(false);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		final Queue<String> names = new LinkedList<>();
		names.add("Dominic");
		names.add("Alex");
		names.add("Marc");
		names.add("Mohamed");
		
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = names.remove();
				names.add(name);
				add(new InvitePanel(name));
				revalidate();	
			}
		});//.start();
	}
	
}
