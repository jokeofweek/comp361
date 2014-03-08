package java2D;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;

import comp361.client.GameClient;
import comp361.client.data.MoveType;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Field;
import comp361.shared.data.Game;

public class GamePanel extends ClientPanel {
	
	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(new Game("p1", "p2", System.currentTimeMillis()));
	}

	public GamePanel(GameClient gameClient, ClientWindow clientWindow, Game game) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI(game);
	}

	private void initUI(Game game) {
		setLayout(new BorderLayout());

		final SelectionContext context = new SelectionContext();
		add(new GameFieldPanel(game, context, true), BorderLayout.CENTER);
		add(new ShipInfoPanel(context), BorderLayout.WEST );
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
