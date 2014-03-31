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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comp361.client.GameClient;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.ArmorType;
import comp361.shared.data.CannonType;
import comp361.shared.data.MoveType;
import comp361.shared.packets.shared.GameMovePacket;
import comp361.shared.packets.shared.RequestSavePacket;

public class ShipInfoPanel extends JPanel implements Observer {

	private SelectionContext context;
	private GameClient client;
	private ClientWindow window;
	
	private final int WIDTH = Constants.SCREEN_WIDTH - (Constants.MAP_WIDTH * Constants.TILE_SIZE);
	
	public ShipInfoPanel(GameClient client, SelectionContext context, ClientWindow window) {
		super();
		
		this.client = client;
		this.context = context;
		this.window = window;
		
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
		
		setLayout(new BorderLayout());
		
		// Create a panel to hold info on the ship.
	
		if (context.getShip() != null) {
			// Build the ship label container
			JPanel labelContainer = new JPanel(new GridLayout(0, 1, 0, 0));
			
			JLabel shipNameLabel = new JLabel(context.getShip().getName());
			shipNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			labelContainer.add(shipNameLabel);

			// Build list of weapons
			List<String> weapons = new ArrayList<>();
			if (context.getShip().getCannonType() != CannonType.NONE) {
				weapons.add(context.getShip().getCannonType() == CannonType.HEAVY ? "Heavy Cannon" : "Cannon");
			}
			if (context.getShip().hasTorpedoes()) {
				weapons.add("Torpedoes");
			}
			if (context.getShip().canKamikaze()) {
				weapons.add("Special");
			}
			if (weapons.size() > 0) {
				String weaponStr = weapons.get(0);
				weapons.remove(0);
				for (String weapon : weapons) {
					weaponStr += ", " + weapon;
				}
				labelContainer.add(new JLabel(weapons.size() == 0 ? "Weapon:" : "Weapons:"));
				labelContainer.add(new JLabel(weaponStr));
			}
			
			// Build armor label
			labelContainer.add(new JLabel("Armor: " + (context.getShip().getArmor() == ArmorType.HEAVY ? "Heavy" : "Normal")));
			
			// If the ship is a mine layer, add info about number of mines
			if (context.getShip().isMineLayer()) {
				JLabel mineLabel = new JLabel("Mines: " + context.getShip().getMineCount());
				labelContainer.add(mineLabel);
			}
			
			
			add(labelContainer, BorderLayout.NORTH);
			
			// Build all the action buttons
			List<JButton> actionButtons = new ArrayList<>();
			
			if (context.getShip().canMove()) {
				JButton moveShipButton = new JButton("Move");
				moveShipButton.addActionListener(new MoveContextActionListener(MoveType.MOVE));
				actionButtons.add(moveShipButton);
			}
			
			// If the ship isn't size 1, then we can turn
			if (context.getShip().getSize() > 1) {
				JButton turnShipButton = new JButton("Turn");
				turnShipButton.addActionListener(new MoveContextActionListener(MoveType.TURN));
				actionButtons.add(turnShipButton);
			}
			
			if (context.getShip().getCannonType() != CannonType.NONE) {
				JButton fireCannonButton = new JButton("Fire Cannon");
				fireCannonButton.addActionListener(new MoveContextActionListener(MoveType.CANNON));
				actionButtons.add(fireCannonButton);
			}
			
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
			
			if (context.getShip().canKamikaze()) {
				JButton fireCannonButton = new JButton("Kamikaze");
				fireCannonButton.addActionListener(new MoveContextActionListener(MoveType.KAMIKAZE));
				actionButtons.add(fireCannonButton);
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
			

			if (context.getShip().hasLongRangeRadar()) {
				JButton toggleLongRangeRadar = new JButton(context.getShip().isLongRangeRadarEnabled() ? "Disable LRR" : "Enable LRR");
				toggleLongRangeRadar.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GameMovePacket packet = new GameMovePacket();
						packet.ship = client.getGameManager().getGame().getShips().indexOf(context.getShip());
						packet.moveType = MoveType.TOGGLE_LONG_RANGE_RADAR;
						client.getGameManager().applyMove(packet, true);
					}
				});
				actionButtons.add(toggleLongRangeRadar);	
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
			JLabel label = new JLabel("No selected ship");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label);
		}
		
		JButton saveButton = new JButton("Request Save");
		SwagFactory.style(saveButton);
		saveButton.addActionListener(new SaveActionListener());
		add(saveButton, BorderLayout.SOUTH);
		
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
	
	private class SaveActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			window.setPanel(new WaitingForSavePanel(client, window, window.getPanel()));
			// Send the save packet
			RequestSavePacket packet = new RequestSavePacket();
			packet.requester = client.getPlayerName();
			client.getClient().sendTCP(packet);
		}
	}
	
}
