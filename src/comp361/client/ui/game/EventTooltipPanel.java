package comp361.client.ui.game;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.data.EventTooltipContext;
import comp361.client.data.event.GameEvent;
import comp361.shared.Constants;

public class EventTooltipPanel extends JPanel implements Observer {

	private JPanel tooltipPanel;
	private EventTooltipContext context;
 
	public EventTooltipPanel(EventTooltipContext context) {

		Dimension d = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setOpaque(false);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		setBounds(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		
		setLayout(null);
		tooltipPanel = new JPanel();
		tooltipPanel.setBackground(Color.blue);
		tooltipPanel.add(new JLabel("Hombre."));
		tooltipPanel.setVisible(false);
		add(tooltipPanel);
		
		context.addObserver(this);
		
	}
	
	@Override
	public void update(Observable o, final Object arg) {
		if (o instanceof EventTooltipContext) {		
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					GameEvent event = (GameEvent)arg;
					if (event == null) {
						tooltipPanel.setVisible(false);
					} else {
						tooltipPanel.setVisible(true);
						tooltipPanel.setBounds(300, 300, 200, 200);
					}
					revalidate();
				}
			});
		}
	}
	
	
}
