package java2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import comp361.client.GameClient;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ResourceManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Direction;
import comp361.shared.data.Game;
import comp361.shared.data.MoveType;
import comp361.shared.data.Ship;
import comp361.shared.packets.shared.GameMovePacket;

public class GameFieldPanel extends JPanel implements Observer {

	private static boolean GOD_MODE = false;
	
	private GameClient client;
	private Game game;
	private boolean isP1;
	private SelectionContext context;
	private long lastImageUpdate = 0;

	public GameFieldPanel(GameClient client, SelectionContext context, boolean isP1) {
		SwagFactory.style(this);

		this.client = client;
		this.game = client.getGameManager().getGame();

		// Set dimensions
		Dimension d = new Dimension(Constants.TILE_SIZE * Constants.MAP_WIDTH,
				Constants.TILE_SIZE * Constants.MAP_HEIGHT);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);

		this.isP1 = isP1;
		this.context = context;
		this.context.addObserver(this);

		this.addMouseListener(new MouseAdapter());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;

		List<Ship> ownShips = game.getPlayerShips(isP1 ? game.getP1() : game.getP2());

		drawWater(g2d);
		drawRectangles(g2d);

		// Draw ship outline for selected ship
		if (context.getShip() != null) {
			drawShipSelection(g2d, context.getShip());
		}

		// Calculate the field of vision
		Set<Point> fov = getFieldOfVision(ownShips);
		drawShips(g2d, fov);
		if (!GOD_MODE) {
			drawFogOfWar(g2d, fov);
		}
		drawSelectionContext(g2d);
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

		for (Ship ship : game
				.getPlayerShips(isP1 ? game.getP1() : game.getP2())) {
			drawShip(g, ship, true, fov);
		}

		for (Ship ship : game.getPlayerShips(!isP1 ? game.getP1() : game
				.getP2())) {
			drawShip(g, ship, false, fov);
		}
	}

	public void drawWater(Graphics g) {
		for (int x = 0; x < game.getField().getCellTypeArray().length; x++) {
			for (int y = 0; y < game.getField().getCellTypeArray()[x].length; y++) {
				Image waterImage = ResourceManager.getInstance().getWaterImage().getImage();
				g.drawImage(waterImage, x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
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

				// If the type is water, skip
				if (type == CellType.WATER) {
					continue;
				}

				Color fillColor = null;
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

	public void drawShip(Graphics g, Ship ship, boolean isOwnShip,
			Set<Point> fov) {

		ResourceManager rm = ResourceManager.getInstance();
		// Get the ship's line
		List<Point> points = ship.getShipLine().getPoints();
		Direction d = ship.getDirection();
		// Render the head
		Point head = points.remove(points.size() - 1);
		if (isOwnShip || fov.contains(head) || GOD_MODE) {
			g.drawImage(rm.getHeadImage(ship, isOwnShip), (int) head.getX()
					* Constants.TILE_SIZE, (int) head.getY()
					* Constants.TILE_SIZE, null);
		}

		// Render the tail
		Point tail = points.remove(0);
		if (isOwnShip || fov.contains(tail) || GOD_MODE) {
			g.drawImage(rm.getTailImage(ship, isOwnShip), (int) tail.getX()
					* Constants.TILE_SIZE, (int) tail.getY()
					* Constants.TILE_SIZE, null);
		}

		// Render the body
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (isOwnShip || fov.contains(p) || GOD_MODE) {
				g.drawImage(rm.getBodyImage(ship, i+1), (int) p.getX()
						* Constants.TILE_SIZE, (int) p.getY()
						* Constants.TILE_SIZE, null);
			}
		}
	}

	private void drawShipSelection(Graphics2D g, Ship ship) {
		Line2D line = ship.getShipLine();
		g.setColor(Constants.SHIP_SELECTION_COLOR);
		g.fillRect(Math.min((int) line.getX1(), (int) line.getX2())
				* Constants.TILE_SIZE,
				Math.min((int) line.getY1(), (int) line.getY2())
						* Constants.TILE_SIZE,
				(Math.abs((int) line.getX1() - (int) line.getX2()) + 1)
						* Constants.TILE_SIZE,
				(Math.abs((int) line.getY1() - (int) line.getY2()) + 1)
						* Constants.TILE_SIZE);

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

	private void drawSelectionSquare(Graphics2D g, int x, int y) {
		g.setColor(Constants.MOVE_COLOR);
		g.fillRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
		g.setColor(Constants.MOVE_BORDER_COLOR);
		g.drawRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
	}
	
	private void drawSelectionContext(Graphics2D g) {
		if (context.getShip() != null) {
			if (context.getType() == MoveType.TORPEDO) {
				// Render a move square at each 
				for (Point p : context.getShip().getTorpedoLine().getPoints()) {
					drawSelectionSquare(g, p.x, p.y);
				}
			} else if (context.getType() == MoveType.CANNON) {
				// Get the cannon range
				Rectangle cannonRangeRect = context.getShip().getCannonRange().getRectangle(context.getShip());
				for (int x = 0; x < cannonRangeRect.getWidth(); x++) {
					for (int y = 0; y < cannonRangeRect.getHeight(); y++) {
						drawSelectionSquare(g, cannonRangeRect.x + x, cannonRangeRect.y + y);							
					}
				}
			} else if (context.getType() == MoveType.MOVE) {
				for (Point p : context.getShip().getValidMovePoints()) {
					drawSelectionSquare(g, p.x, p.y);
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}

	private class MouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// Get the tile that we pressed
			int x = e.getX() / Constants.TILE_SIZE;
			int y = e.getY() / Constants.TILE_SIZE;
			
			// See if we selected one of our ships
			Point p = new Point(x, y);
			for (Ship s : game.getPlayerShips(isP1 ? game.getP1() : game.getP2())) {
				if (s.pointBelongsToShip(p)) {
					// Select the ship!
					context.setShip(s);
					return;
				}
			}
			
			// See if we have a selection context and we clicked on it
			if (context.getShip() != null && context.getType() != null) {
				if (context.getType() == MoveType.MOVE) {
					if (context.getShip().getValidMovePoints().contains(p)) {
						sendMove(p);
					}
				} else if (context.getType() == MoveType.CANNON) {
					if (context.getShip().getCannonRange().inRange(context.getShip(), p.x, p.y)) {
						sendMove(p);
					}
				}
			}
		}
	}
	
	private void sendMove(Point p) {
		GameMovePacket packet = new GameMovePacket();
		packet.ship = client.getGameManager().getGame().getShips().indexOf(context.getShip());
		packet.moveType = MoveType.MOVE;
		packet.contextPoint = p;
		client.getGameManager().applyMove(packet, true);
	}
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		long now = System.currentTimeMillis();
		
		if (now - lastImageUpdate > 100) {
			repaint();
			lastImageUpdate = now;
		}
		
		return true;
	}
}