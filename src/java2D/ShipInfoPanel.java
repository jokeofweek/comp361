package java2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import comp361.client.data.SelectionContext;
import comp361.shared.Constants;

public class ShipInfoPanel extends JPanel implements Observer {

	private SelectionContext context;
	private JPanel infoPanel = new JPanel();
	
	public ShipInfoPanel(SelectionContext context) {
		super(new BorderLayout());
		
		this.context = context;
		context.addObserver(this);
		
		Dimension d = new Dimension(Constants.SCREEN_WIDTH - (Constants.MAP_WIDTH * Constants.TILE_SIZE), Constants.SCREEN_HEIGHT);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		
		infoPanel = new JPanel();
		add(infoPanel);
		
		refreshData();
	}
	
	private void refreshData() {
		infoPanel.removeAll();
		
		if (context.getShip() != null) {
			infoPanel.add(new JLabel(context.getShip().getName()));
		}
		
		revalidate();
		repaint();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		refreshData();
	}
	
}
