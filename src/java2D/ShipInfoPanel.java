package java2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp361.client.data.MoveType;
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
		
		infoPanel.setLayout(new GridLayout(0, 1));
		
		if (context.getShip() != null) {
			infoPanel.add(new JLabel(context.getShip().getName()));

			JButton moveShipButton = new JButton("Move");
			moveShipButton.addActionListener(new MoveContextActionListener(MoveType.MOVE));
			infoPanel.add(moveShipButton);
			JButton turnShipButton = new JButton("Turn");
			infoPanel.add(turnShipButton);
			JButton fireCannonButton = new JButton("Fire Cannon");
			moveShipButton.addActionListener(new MoveContextActionListener(MoveType.CANNON));
			infoPanel.add(fireCannonButton);
			
			if (context.getShip().hasTorpedoes()) {
				JButton fireTorpedoButton = new JButton("Fire Torpedo");
				infoPanel.add(fireTorpedoButton);	
			}
						
		} else {
			infoPanel.add(new JLabel("No selected ship"));
		}
		
		revalidate();
		repaint();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		refreshData();
	}
	
	private class MoveContextActionListener implements ActionListener {
		private MoveType type;
		
		public MoveContextActionListener(MoveType type) {
			this.type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			context.setType(type);
		}
	}
	
}
