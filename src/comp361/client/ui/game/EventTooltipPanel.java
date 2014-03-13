package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.data.EventTooltipContext;
import comp361.client.data.event.Effect;
import comp361.client.data.event.GameEvent;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.Game;

public class EventTooltipPanel extends JPanel implements Observer {

	private JPanel tooltipPanel;
	private EventTooltipContext context;
	private final static int BORDER_SPACING = 10;
 
	public EventTooltipPanel(EventTooltipContext context) {

		Dimension d = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setOpaque(false);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		setBounds(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		
		setLayout(null);
		tooltipPanel = new JPanel(new BorderLayout());
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
						tooltipPanel.removeAll();
						
						JPanel labelContainer = new JPanel();
						labelContainer.setLayout(new BoxLayout(labelContainer, BoxLayout.Y_AXIS));
						
						// Update the panel text
						JLabel headerLabel = new JLabel("Impact Detected");
						headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						SwagFactory.style(headerLabel);
						headerLabel.setFont(SwagFactory.HEADER_FONT);
						labelContainer.add(headerLabel);
						
						if (event.getCause() != null) {
							JLabel causeLabel = new JLabel("Cause: " + event.getCause());
							causeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
							SwagFactory.style(causeLabel);
							labelContainer.add(causeLabel);
						}
						JLabel effectLabel = new JLabel(event.getEffect().toString());
						if (event.getEffect() == Effect.SHIP_SUNK) {
							effectLabel.setText(event.getEffect().toString().replace("#", event.getVictimShip().getName()));
						}
						effectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						SwagFactory.style(effectLabel);
						labelContainer.add(effectLabel);
						
						tooltipPanel.add(labelContainer);
						
						SwagFactory.style(tooltipPanel);
						tooltipPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
								BorderFactory.createEmptyBorder(BORDER_SPACING, BORDER_SPACING, BORDER_SPACING, BORDER_SPACING)));
						tooltipPanel.setVisible(true);
						
						
						tooltipPanel.doLayout();
						int tooltipWidth = tooltipPanel.getPreferredSize().width;
						int tooltipHeight = tooltipPanel.getPreferredSize().height;
						
						// Setup the bounds based on the event position
						int top;
						// Initial offset (want to render past the top left bar)
						int left = Constants.SCREEN_WIDTH - (Constants.TILE_SIZE * Constants.MAP_WIDTH);
						if (event.getPoint().y < (Constants.MAP_HEIGHT / 2)) {
							top = (event.getPoint().y + 1) * Constants.TILE_SIZE;
						} else {
							top = (event.getPoint().y) * Constants.TILE_SIZE - tooltipHeight;
						}
						left += ((event.getPoint().x + 0.5) * Constants.TILE_SIZE) - (tooltipWidth / 2);
						// Make sure we don't go off to the left
						left = Math.min(left, Constants.SCREEN_WIDTH - tooltipWidth);
						
						tooltipPanel.setBounds(left, top, tooltipWidth, tooltipHeight);
					}
					revalidate();
				}
			});
		}
	}
	
	
}
