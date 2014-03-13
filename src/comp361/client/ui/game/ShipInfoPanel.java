package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comp361.client.GameClient;
import comp361.client.data.SelectionContext;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.MoveType;
import comp361.shared.packets.shared.GameMovePacket;

public class ShipInfoPanel extends JPanel implements Observer {

	private SelectionContext context;
	private GameClient client;
	private final int WIDTH = Constants.SCREEN_WIDTH - (Constants.MAP_WIDTH * Constants.TILE_SIZE);
	
	public ShipInfoPanel(GameClient client, SelectionContext context) {
		super();
		
		this.client = client;
		this.context = context;
		
				
		refreshData();
	}
	
	@Override
	public int getWidth() {
		return WIDTH;
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, super.getPreferredSize().height);
	}
	
	public void refreshData() {
		removeAll();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Create a panel to hold info on the ship.
	
		if (context.getShip() != null) {
			// Build the ship label container
			JPanel labelContainer = new JPanel(new GridLayout(0, 1));
			JLabel shipNameLabel = new JLabel(context.getShip().getName());
			shipNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			labelContainer.add(shipNameLabel);
			// If the ship is a mine layer, add info about number of mines
			if (context.getShip().isMineLayer()) {
				JLabel mineLabel = new JLabel("Mines: " + context.getShip().getMineCount());
				mineLabel.setHorizontalAlignment(SwingConstants.CENTER);
				labelContainer.add(mineLabel);
			}
			
			add(labelContainer);
			
			// Build all the action buttons
			List<JButton> actionButtons = new ArrayList<>();
			
			JButton moveShipButton = new JButton("Move");
			moveShipButton.addActionListener(new MoveContextActionListener(MoveType.MOVE));
			actionButtons.add(moveShipButton);
			
			JButton turnShipButton = new JButton("Turn");
			actionButtons.add(turnShipButton);
			
			JButton fireCannonButton = new JButton("Fire Cannon");
			fireCannonButton.addActionListener(new MoveContextActionListener(MoveType.CANNON));
			actionButtons.add(fireCannonButton);
			
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
				actionButtons.add(fireTorpedoButton);	
			}
			
			// If we have mines and there is a place we can drop the mine, show the button
			if (context.getShip().isMineLayer() && context.getShip().getMineCount() > 0 && !context.getShip().getValidMineDropPoints().isEmpty()) {
				JButton dropMineButton = new JButton("Drop Mine");
				dropMineButton.addActionListener(new MoveContextActionListener(MoveType.DROP_MINE));
				actionButtons.add(dropMineButton);
			}
			
			// If we can pickup mines and there is a mine to pickup, show the button
			if (context.getShip().isMineLayer()) {
				if (context.getShip().getValidMinePickupPoints().size() > 0) {
					JButton pickupMineButton = new JButton("Pickup Mine");
					pickupMineButton.addActionListener(new MoveContextActionListener(MoveType.PICKUP_MINE));
					actionButtons.add(pickupMineButton);
				}
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
				actionButtons.add(repairButton);
			}
			
			// Style and add all the buttons to a container
			JPanel buttonContainer = new JPanel();
			for (JButton button : actionButtons) {
				buttonContainer.add(button);
				SwagFactory.styleButtonWithoutHeight(button);
				// SwagFactory.style(button);
				// SwagFactory.styleButtonHeight(button, button.getWidth());
			}
			
			add(buttonContainer);
		} else {
			JPanel labelContainer = new JPanel(new BorderLayout());
			JLabel label = new JLabel("No selected ship");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			labelContainer.add(label);
			add(labelContainer);
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
