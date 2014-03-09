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

import comp361.client.GameClient;
import comp361.client.data.SelectionContext;
import comp361.shared.Constants;
import comp361.shared.data.MoveType;
import comp361.shared.packets.shared.GameMovePacket;

public class ShipInfoPanel extends JPanel implements Observer {

	private SelectionContext context;
	private JPanel infoPanel = new JPanel();
	private GameClient client;
	
	public ShipInfoPanel(GameClient client, SelectionContext context) {
		super(new BorderLayout());
		
		this.client = client;
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
	
	public void refreshData() {
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
			fireCannonButton.addActionListener(new MoveContextActionListener(MoveType.CANNON));
			infoPanel.add(fireCannonButton);
			
			if (context.getShip().hasTorpedoes()) {
				JButton fireTorpedoButton = new JButton("Fire Torpedo");
				// On fire torpedo, build a game move packet and apply it.
				fireTorpedoButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GameMovePacket packet = new GameMovePacket();
						packet.ship = client.getGameManager().getGame().getShips().indexOf(context.getShip());
						packet.moveType = MoveType.TORPEDO;
						client.getGameManager().applyMove(packet, true);
					}
				});
				infoPanel.add(fireTorpedoButton);	
			}
			
			// If we can repair the ship, add the repair button
			if (client.getGameManager().getGame().canRepair(context.getShip())) {
				JButton repairButton = new JButton("Repair");
				repairButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						GameMovePacket packet = new GameMovePacket();
						packet.ship = client.getGameManager().getGame().getShips().indexOf(context.getShip());
						packet.moveType = MoveType.REPAIR;
						client.getGameManager().applyMove(packet, true);
					}
				});
				infoPanel.add(repairButton);
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
