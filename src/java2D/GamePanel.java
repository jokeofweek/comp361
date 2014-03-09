package java2D;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;

public class GamePanel extends ClientPanel {
	
	private SelectionContext context;
	private GameFieldPanel fieldPanel;
	private ShipInfoPanel infoPanel;
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(gameClient);
	}

	private void initUI(GameClient client) {
		setLayout(new BorderLayout());

		context = new SelectionContext();
		fieldPanel = new GameFieldPanel(client, context, client.getGameManager().isPlayer1());
		add(fieldPanel, BorderLayout.CENTER);
		
		infoPanel = new ShipInfoPanel(client, context); 
		add(infoPanel, BorderLayout.WEST );
	}

	@Override
	public void enter() {
		getGameClient().getGameManager().addObserver(this);
	}
	
	@Override
	public void exit() {
		getGameClient().getGameManager().deleteObserver(this);
	}
	
	@Override
	public void update(Observable source, Object object) {
		// If it is no longer our turn, clear the context
		if (context.getShip() != null && context.getType() != null && source instanceof GameManager) {
			if (!((GameManager)source).isTurn()) {
				context.setShip(null);
			}
		}
		infoPanel.refreshData();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				revalidate();
				repaint();
			}
		});
	}

}
