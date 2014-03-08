package java2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Set;

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
	private boolean isP1;

	GameFieldPanel(Game game, boolean isP1) {
		SwagFactory.style(this);
		this.game = game;

		// Set dimensions
		Dimension d = new Dimension(Constants.TILE_SIZE * Constants.MAP_WIDTH,
				Constants.TILE_SIZE * Constants.MAP_HEIGHT);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);

		this.isP1 = isP1;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		List<Ship> ownShips = game.getPlayerShips(game.getP1());

		drawRectangles(g2d);

		// Calculate the field of vision
		Set<Point> fov = getFieldOfVision(ownShips);
		drawShips(g2d, fov);
		drawFogOfWar(g2d, fov);
	}

	private Set<Point> getFieldOfVision(List<Ship> ships) {
		Set<Point> points = game.getPointsVisibleFromBase(isP1 ? game.getP1()
				: game.getP2());

		// Add all the points visible from the ship
		for (int x = 0; x < Constants.MAP_WIDTH; x++) {
			for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
				Point p = new Point(x, y);
				if (!points.contains(p)) {
					for (Ship s : ships) {
						if (s.getActiveRadar().inRange(s, x, y)) {
							points.add(p);
						}
					}
				}
			}
		}

		return points;
	}

	private void drawShips(Graphics g, Set<Point> fov) {
		ResourceManager rm = ResourceManager.getInstance();

		for (Ship ship : game.getPlayerShips(isP1 ? game.getP1() : game.getP2())) {
			renderShip(g, ship, true, fov);
		}
		
		for (Ship ship : game.getPlayerShips(!isP1 ? game.getP1() : game.getP2())) {
			renderShip(g, ship, false, fov);
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

	public void renderShip(Graphics g, Ship ship, boolean isOwnShip,
			Set<Point> fov) {

		ResourceManager rm = ResourceManager.getInstance();
		// Get the ship's line
		List<Point> points = ship.getShipLine().getPoints();
		Direction d = ship.getDirection();
		// Render the head
		Point head = points.remove(points.size() - 1);
		if (isOwnShip || fov.contains(head)) {
			g.drawImage(rm.getHeadImage(d), (int) head.getX()
					* Constants.TILE_SIZE, (int) head.getY()
					* Constants.TILE_SIZE, null);
		}

		// Render the tail
		Point tail = points.remove(0);
		if (isOwnShip || fov.contains(tail)) {
			g.drawImage(rm.getTailImage(d), (int) tail.getX()
					* Constants.TILE_SIZE, (int) tail.getY()
					* Constants.TILE_SIZE, null);
		}

		// Render the body
		for (Point p : points) {
			if (isOwnShip || fov.contains(p)) {
				g.drawImage(rm.getBodyImage(d), (int) p.getX()
						* Constants.TILE_SIZE, (int) p.getY()
						* Constants.TILE_SIZE, null);
			}
		}
	}

	public void drawFogOfWar(Graphics2D g, Set<Point> fov) {
		g.setColor(Constants.FOG_OF_WAR_COLOR);
		for (int x = 0; x < Constants.MAP_WIDTH; x++) {
			for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
				// If the point isn't in the FOV, then we render the fog of war
				if (!fov.contains(new Point(x, y))) {
					g.fillRect(x * Constants.TILE_SIZE,
							y * Constants.TILE_SIZE, Constants.TILE_SIZE,
							Constants.TILE_SIZE);
				}
			}
		}
	}
}