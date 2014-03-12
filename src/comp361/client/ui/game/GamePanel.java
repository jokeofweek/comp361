package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.client.ui.util.HealthBar;
import comp361.shared.data.GameResult;
import comp361.shared.data.Ship;
import comp361.shared.packets.shared.GameOverPacket;

public class GamePanel extends ClientPanel {
	
	private SelectionContext context;
	private JLabel turnLabel;
	private GameFieldPanel fieldPanel;
	private ShipInfoPanel infoPanel;
	private List<HealthBar> healthBars;
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		
		initUI(gameClient);
		
		// Add this as an observer of the context
		context.addObserver(this);
	}

	private void initUI(final GameClient client) {
		setLayout(new BorderLayout());

		context = new SelectionContext();
		fieldPanel = new GameFieldPanel(client, context, client.getGameManager().isPlayer1());
		add(fieldPanel, BorderLayout.CENTER);
		
		JPanel leftBarPanel = new JPanel(new BorderLayout());
		
		// Container for the turn info and health info
		JPanel gameInfoPanel = new JPanel(new BorderLayout());
		turnLabel = new JLabel();
		turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		updateTurnLabel();
		gameInfoPanel.add(turnLabel, BorderLayout.NORTH);
		
		// Buid health bars for every player
		JPanel healthPanel = new JPanel(new GridLayout(4, 1));
		healthBars = new ArrayList<>();
		for (final String player : new String[]{client.getGameManager().getGame().getP1(), client.getGameManager().getGame().getP2()}) {
			healthPanel.add(new JLabel(player + ":"));
			HealthBar bar = (new HealthBar(10) {
				
				@Override
				public int getValue() {
					int remainingHealth = 0;
					for (Ship s : client.getGameManager().getGame().getPlayerShips(player)) {
						for (int i = 0; i < s.getSize(); i++) {
							remainingHealth += s.getHealth(i);
						}
					}
					return remainingHealth;
				}
				
				@Override
				public int getMaxValue() {
					int totalHealth = 0;
					for (Ship s : client.getGameManager().getGame().getPlayerShips(player)) {
						totalHealth += s.getMaxHealthPerSquare() * s.getSize();
					}
					return totalHealth;
				}
			});
			healthBars.add(bar);
			healthPanel.add(bar);
		}
		
		gameInfoPanel.add(healthPanel);
		leftBarPanel.add(gameInfoPanel, BorderLayout.NORTH);
		
		// Container for the ship info
		infoPanel = new ShipInfoPanel(client, context);
		leftBarPanel.add(infoPanel, BorderLayout.CENTER);
		 
		add(leftBarPanel, BorderLayout.WEST );
	}

	@Override
	public void enter() {
		getGameClient().getGameManager().addObserver(this);
	}
	
	@Override
	public void exit() {
		getGameClient().getGameManager().deleteObserver(this);
	}
	
	private void updateTurnLabel() {
		turnLabel.setText(getGameClient().getGameManager().isTurn() ? "Your turn" : "Enemy's turn");
	}
	
	@Override
	public void update(Observable source, Object object) {
		// If we received a game over packet
		if (object instanceof GameOverPacket) {
			// As long as we didn't disconnect (and therefore lose), show a message
			GameOverPacket packet = (GameOverPacket) object;
			if (packet.result != GameResult.LOSS || !packet.fromDisconnect) {
				JOptionPane.showMessageDialog(null, packet.message);
				getClientWindow().setPanel(new LobbyPanel(getGameClient(), getClientWindow()));
			}
			return;
		}
		
		// If it is no longer our turn, clear the context
		if (context.getShip() != null && context.getType() != null && source instanceof GameManager) {
			if (!((GameManager)source).isTurn()) {
				context.setShip(null);
			}
		}
		// If the ship in the context is sunk, clear it
		if (context.getShip() != null && context.getShip().isSunk()) {
			context.setShip(null);
		}
		// If a turn passed, update the context points
		if (source instanceof GameManager) {
			context.updatePoints();
		}
		// Update the info panel
		updateTurnLabel();
		
		// Notify observers
		infoPanel.update(source, object);
		fieldPanel.update(source, object);
		for (HealthBar bar : healthBars) {
			bar.update(source, object);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				revalidate();
				repaint();
			}
		});
	}

	
	
}
