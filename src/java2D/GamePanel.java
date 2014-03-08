package java2D;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.Observable;

import comp361.client.GameClient;
import comp361.client.ui.ClientPanel;
import comp361.client.ui.ClientWindow;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Field;
import comp361.shared.data.Game;

public class GamePanel extends ClientPanel {

	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());

		Game g = new Game("p1", "p2", System.currentTimeMillis());
		add(new GameFieldPanel(g), BorderLayout.CENTER);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
