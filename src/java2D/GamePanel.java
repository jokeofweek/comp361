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

public class GamePanel extends ClientPanel {

	public GamePanel(GameClient gameClient, ClientWindow clientWindow) {
		super(gameClient, clientWindow, new BorderLayout());
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());

		Field testField = new Field(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);

		for (int x = 0; x < testField.getCellTypeArray().length; x = x + 2) {
			for (int y = 0; y < testField.getCellTypeArray()[x].length; y = y + 2) {
				Point point = new Point(x, y);
				testField.setCellType(point, CellType.MINE);
			}
		}

		add(new GameFieldPanel(testField), BorderLayout.CENTER);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
