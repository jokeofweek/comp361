package comp361.client.ui.game;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.client.ui.lobby.LobbyPanel;
import comp361.shared.data.GameResult;
import comp361.shared.packets.shared.GameOverPacket;

public class GamePanel extends ClientPanel {
	
	private SelectionContext context;
	private JLabel turnLabel;
	private GameFieldPanel fieldPanel;
	private ShipInfoPanel infoPanel;
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(gameClient);
		
		// Add this as an observer of the context
		context.addObserver(this);
	}

	private void initUI(GameClient client) {
		setLayout(new BorderLayout());

		context = new SelectionContext();
		fieldPanel = new GameFieldPanel(client, context, client.getGameManager().isPlayer1());
		add(fieldPanel, BorderLayout.CENTER);
		
		JPanel leftBarPanel = new JPanel(new BorderLayout());
		
		turnLabel = new JLabel();
		updateTurnLabel();
		leftBarPanel.add(turnLabel, BorderLayout.NORTH);
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
		infoPanel.update(source, object);
		fieldPanel.update(source, object);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				revalidate();
				repaint();
			}
		});
	}

}
