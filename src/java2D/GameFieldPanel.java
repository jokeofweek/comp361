package java2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Field;
import comp361.shared.data.Game;
import comp361.shared.data.Ship;

public class GameFieldPanel extends JPanel {

	private Game game;

	GameFieldPanel(Game game) {
		SwagFactory.style(this);
		this.game = game;

		// Set dimensions
		Dimension d = new Dimension(Constants.TILE_SIZE * Constants.MAP_WIDTH,
				Constants.TILE_SIZE * Constants.MAP_HEIGHT);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		drawRectangles(g2d);
	}

	private void drawShips(Graphics g, ArrayList<Ship> ships) {
		for (Ship ship : ships) {

		}
	}

	public void drawRectangles(Graphics2D g) {
		for (int x = 0; x < game.getField().getCellTypeArray().length; x++) {
			for (int y = 0; y < game.getField().getCellTypeArray()[x].length; y++) {
				Rectangle2D rect = new Rectangle2D.Double(x
						* Constants.TILE_SIZE, y * Constants.TILE_SIZE,
						Constants.TILE_SIZE, Constants.TILE_SIZE);

				CellType type = game.getField().getCellTypeArray()[(int) x][(int) y];

				Color fillColor = Color.blue;
				switch (type) {
				case BASE:
					fillColor = Color.green;
					break;
				case MINE:
					fillColor = Color.red;
					break;
				case REEF:
					fillColor = Color.black;
					break;
				}

				g.setColor(fillColor);
				g.draw(rect);
				g.fill(rect);

			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

	}
}