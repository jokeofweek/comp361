package java2D;

import java.awt.BorderLayout;
import java.util.Observable;

import comp361.client.GameClient;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.shared.data.Game;

public class GamePanel extends ClientPanel {
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(new Game("p1", "p2", System.currentTimeMillis()), true);
	}

	public GamePanel(GameClient gameClient, ClientWindow clientWindow, Game game) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(game, gameClient.getPlayerName().equals(game.getP1()));
	}

	private void initUI(Game game, boolean isP1) {
		setLayout(new BorderLayout());

		final SelectionContext context = new SelectionContext();
		add(new GameFieldPanel(game, context, isP1), BorderLayout.CENTER);
		add(new ShipInfoPanel(context), BorderLayout.WEST );
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
