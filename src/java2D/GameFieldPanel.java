package java2D;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.client.GameClient;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.ui.ResourceManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Game;
import comp361.shared.data.MoveType;
import comp361.shared.data.Ship;
import comp361.shared.packets.shared.GameMovePacket;

public class GameFieldPanel extends JPanel implements Observer {

	private static boolean GOD_MODE = false;

	private GameClient client;
	private Game game;
	private long lastImageUpdate = 0;
	private boolean isP1;
	private SelectionContext context;

	// Cached field of vision
	private Set<Point> fov;
	private Set<Point> sonarFov;

	// The cursor position
	int cursorX = -1;
	int cursorY = -1;

	public GameFieldPanel(GameClient client, SelectionContext context,
			boolean isP1) {
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
		this.addMouseListener(new FieldMouseAdapter());
		this.addMouseMotionListener(new FieldMouseAdapter());

		// Calculate the FOV
		recalculateFieldsOfVision();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;

		// Draw CellType tiles (BASE, MINE, REEF, WATER)
		drawTiles(g2d, sonarFov);

		// Draw ship outline for selected ship
		if (context.getShip() != null) {
			drawShipSelection(g2d, context.getShip());
		}

		drawShips(g2d, fov);
		if (!GOD_MODE) {
			drawFogOfWar(g2d, fov);
		}
		drawSelectionContext(g2d);
	}

	private void recalculateFieldsOfVision() {
		List<Ship> ships = game.getPlayerShips(isP1 ? game.getP1() : game
				.getP2());
		fov = new HashSet<>();
		sonarFov = new HashSet<>();

		fov.addAll(game.getPointsVisibleFromBase(isP1 ? game.getP1() : game
				.getP2()));

		// Add all the points visible from the ship
		for (int x = 0; x < Constants.MAP_WIDTH; x++) {
			for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
				Point p = new Point(x, y);
				for (Ship s : ships) {
					if (s.getActiveRadar().inRange(s, x, y)) {
						fov.add(p);
						// If this is a sonar ship, add it down there as well.
						if (s.hasSonar()) {
							sonarFov.add(p);
						}
					}
				}
			}

		}
	}

	private void drawShips(Graphics g, Set<Point> fov) {
		for (Ship ship : game
				.getPlayerShips(isP1 ? game.getP1() : game.getP2())) {
			drawShip(g, ship, true, fov);
		}

		for (Ship ship : game.getPlayerShips(!isP1 ? game.getP1() : game
				.getP2())) {
			drawShip(g, ship, false, fov);
		}
	}

	public void drawTiles(Graphics2D g, Set<Point> sonarFov) {
		for (int x = 0; x < game.getField().getCellTypeArray().length; x++) {
			for (int y = 0; y < game.getField().getCellTypeArray()[x].length; y++) {
				CellType type = game.getField().getCellTypeArray()[(int) x][(int) y];

				drawWater(g, x, y);

				// If the point is in the sonar fov, add overlay
				if (sonarFov.contains(new Point(x, y))) {
					g.setColor(Constants.SONAR_COLOR);
					g.fillRect(x * Constants.TILE_SIZE,
							y * Constants.TILE_SIZE, Constants.TILE_SIZE,
							Constants.TILE_SIZE);
				}

				switch (type) {
				case BASE:
					drawBase(g, x, y);
					break;
				case MINE:
					if (GOD_MODE || sonarFov.contains(new Point(x, y))) {
						drawMine(g, x, y);
					}
					break;
				case REEF:
					drawReef(g, x, y);
					break;
				default:
					// Because eclipse likes to whine
				}

			}
		}
	}

	public void drawShip(Graphics g, Ship ship, boolean isOwnShip,
			Set<Point> fov) {

		ResourceManager rm = ResourceManager.getInstance();
		// Get the ship's line
		List<Point> points = ship.getShipLine().getPoints();
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
				g.drawImage(rm.getBodyImage(ship, i + 1), (int) p.getX()
						* Constants.TILE_SIZE, (int) p.getY()
						* Constants.TILE_SIZE, null);
			}
		}
	}

	private void drawReef(Graphics2D g, int x, int y) {
		g.drawImage(ResourceManager.getInstance().getReefImage(), x
				* Constants.TILE_SIZE, y * Constants.TILE_SIZE, null);
	}

	private void drawMine(Graphics2D g, int x, int y) {
		g.drawImage(ResourceManager.getInstance().getMineImage(), x
				* Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
	}

	private void drawBase(Graphics2D g, int x, int y) {
		g.drawImage(ResourceManager.getInstance().getBaseImage(y), x
				* Constants.TILE_SIZE, y * Constants.TILE_SIZE, null);
	}

	public void drawWater(Graphics g, int x, int y) {
		g.drawImage(ResourceManager.getInstance().getWaterImage(), x
				* Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
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
		// If we are where the cursor is, change the color
		if (x == cursorX && y == cursorY) {
			g.setColor(Constants.MOVE_HOVER_COLOR);
		} else {
			g.setColor(Constants.MOVE_COLOR);
		}
		g.fillRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE,
				Constants.TILE_SIZE, Constants.TILE_SIZE);
		g.setColor(Constants.MOVE_BORDER_COLOR);
		g.drawRect(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE,
				Constants.TILE_SIZE, Constants.TILE_SIZE);
	}

	private void drawSelectionContext(Graphics2D g) {
		if (context.getShip() != null) {
			// Render a selection square on all the points for the context
			for (Point p : context.getPoints()) {
				drawSelectionSquare(g, p.x, p.y);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// Only update the field panel if a turn actually passed
		if (o instanceof GameManager) {
			recalculateFieldsOfVision();
		}
	}

	private class FieldMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// Get the tile that we pressed
			int x = e.getX() / Constants.TILE_SIZE;
			int y = e.getY() / Constants.TILE_SIZE;

			// See if we selected one of our ships
			Point p = new Point(x, y);
			for (Ship s : game.getPlayerShips(isP1 ? game.getP1() : game
					.getP2())) {
				if (s.pointBelongsToShip(p)) {
					// Select the ship!
					context.setShip(s);
					return;
				}
			}

			// See if we have a selection context and we clicked on a point in the context
			if (context.getShip() != null && context.getType() != null) {
				if (context.getPoints().contains(p)) {
					sendMove(p);
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// When we exit the component, set the cursor to -1
			cursorX = -1;
			cursorY = -1;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// Calculate the cursor position.
			final int newX = e.getX() / Constants.TILE_SIZE;
			final int newY = e.getY() / Constants.TILE_SIZE;
			
			// Repaint if it changed
			boolean changed = (newX != cursorX || newY != cursorY);
			

			if (changed) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						cursorX = newX;
						cursorY = newY;

						repaint();
					}
				});
			}
		}
	}

	private void sendMove(Point p) {
		GameMovePacket packet = new GameMovePacket();
		packet.ship = client.getGameManager().getGame().getShips()
				.indexOf(context.getShip());
		packet.moveType = context.getType();
		packet.contextPoint = p;
		client.getGameManager().applyMove(packet, true);
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w,
			int h) {
		long now = System.currentTimeMillis();

		if (now - lastImageUpdate > 100) {
			repaint();
			lastImageUpdate = now;
		}

		return true;
	}
}