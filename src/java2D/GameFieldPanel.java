package java2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import comp361.client.ui.ResourceManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Direction;
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
		drawOwnShips(g2d, game.getPlayerShips(game.getP1()));
	}

	private void drawOwnShips(Graphics g, List<Ship> ships) {
		ResourceManager rm = ResourceManager.getInstance();

		for (Ship ship : ships) {
			// Get the ship's line
			List<Point> points = ship.getShipLine().getPoints();
			Direction d = ship.getDirection();
			// Render the head
			Point head = points.remove(points.size() - 1);
			g.drawImage(rm.getHeadImage(d), (int) head.getX()
					* Constants.TILE_SIZE, (int) head.getY()
					* Constants.TILE_SIZE, null);
			
			// Render the tail
			Point tail = points.remove(0);
			g.drawImage(rm.getTailImage(d), (int) tail.getX()
					* Constants.TILE_SIZE, (int) tail.getY()
					* Constants.TILE_SIZE, null);
			
			// Render the body
			for (Point p : points) {
				g.drawImage(rm.getBodyImage(d), (int) p.getX()
						* Constants.TILE_SIZE, (int) p.getY()
						* Constants.TILE_SIZE, null);
			}
		}
	}

	public void drawRectangles(Graphics2D g) {
		for (int x = 0; x < game.getField().getCellTypeArray().length; x++) {
			for (int y = 0; y < game.getField().getCellTypeArray()[x].length; y++) {
				Rectangle2D rect = new Rectangle2D.Double(x
						* Constants.TILE_SIZE, y * Constants.TILE_SIZE,
						Constants.TILE_SIZE, Constants.TILE_SIZE);

				CellType type = game.getField().getCellTypeArray()[(int) x][(int) y];

				// If the type is water, render the image
				if (type == CellType.WATER) {
					g.drawImage(ResourceManager.getInstance().getWaterImage(),
							x * Constants.TILE_SIZE, y * Constants.TILE_SIZE,
							null);
					continue;
				}

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