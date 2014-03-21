package comp361.client.ui.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import comp361.client.GameClient;
import comp361.client.data.EventTooltipContext;
import comp361.client.data.GameManager;
import comp361.client.data.SelectionContext;
import comp361.client.data.event.Cause;
import comp361.client.data.event.Effect;
import comp361.client.data.event.GameEvent;
import comp361.client.resources.ResourceManager;
import comp361.client.resources.SoundManager;
import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Direction;
import comp361.shared.data.Game;
import comp361.shared.data.Ship;
import comp361.shared.packets.shared.GameMovePacket;

public class GameFieldPanel extends JPanel implements Observer {

	private static boolean GOD_MODE = false;
	
	// Transforms
	private static final RescaleOp SUNKEN_SHIP_TRANSFORM = 
			new RescaleOp(new float[]{0f, 0f, 0f, Constants.SUNKEN_SHIP_ALPHA}, new float[4], null);

	private GameClient client;
	private Game game;
	private boolean isP1;
	private SelectionContext context;
	private EventTooltipContext eventContext;
	private List<GameEvent> events = new ArrayList<>();

	private int alphaPulseCounter = 0;
	private boolean alphaPulsingForwards = true;
	private Color[] sonarColors = new Color[Constants.PULSE_ALPHA_INTERVALS];
	
	// Cached field of vision
	private Set<Point> fov;
	private Set<Point> sonarFov;

	// The cursor position
	int cursorX = -1;
	int cursorY = -1;

	public GameFieldPanel(GameClient client, SelectionContext context,
			boolean isP1, EventTooltipContext eventContext) {
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
		this.eventContext = eventContext;
		this.addMouseListener(new FieldMouseAdapter());
		this.addMouseMotionListener(new FieldMouseAdapter());

		// Calculate the FOV
		recalculateFieldsOfVision();
		
		// Setup the pulse colors
		for (int i = 0; i < sonarColors.length; i++) {
			sonarColors[i] = new Color(Constants.SONAR_RED, Constants.SONAR_GREEN,
					Constants.SONAR_BLUE, Constants.SONAR_ALPHA + Constants.PULSE_ALPHA_DELTA * i);
		}
		
		new Timer(150, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (alphaPulsingForwards) {
					if (alphaPulseCounter == Constants.PULSE_ALPHA_INTERVALS - 2) {
						alphaPulsingForwards = false;
					}
					alphaPulseCounter++;
				} else {
					if (alphaPulseCounter == 1) {
						alphaPulsingForwards = true;
					}
					alphaPulseCounter--;
				}
				repaint();
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;

		// Draw CellType tiles (BASE, MINE, REEF, WATER)
		drawTiles(g2d, sonarFov);

		drawLongRangeRadarOutlines(g2d);
		
		// Draw ship outline for selected ship
		if (context.getShip() != null) {
			drawShipSelection(g2d, context.getShip());
		}

		drawShips(g2d, fov);
		if (!GOD_MODE) {
			drawFogOfWar(g2d, fov);
		}
		
		// Draw the game events
		drawGameEvents(g2d, fov);
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
					if (!s.isSunk() && s.getActiveRadar().inRange(s, x, y)) {
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
		// Draw sunken ships first
		Set<Ship> liveShips = new HashSet<>();
		for (Ship ship : game.getShips()) {
			if (ship.isSunk()) {
				drawShip(g, ship, isP1 == ship.getOwner().equals(game.getP1()), true, fov);
			} else {
				liveShips.add(ship);
			}
		}
		
		// Render live ships
		for (Ship ship : liveShips) {
			drawShip(g, ship, isP1 == ship.getOwner().equals(game.getP1()), false, fov);
		}		
	}

	public void drawTiles(Graphics2D g, Set<Point> sonarFov) {
		for (int x = 0; x < game.getField().getCellTypeArray().length; x++) {
			for (int y = 0; y < game.getField().getCellTypeArray()[x].length; y++) {
				CellType type = game.getField().getCellTypeArray()[(int) x][(int) y];

				drawWater(g, x, y);

				// If the point is in the sonar fov, add overlay
				if (sonarFov.contains(new Point(x, y))) {
					g.setColor(sonarColors[alphaPulseCounter]);
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

	public void drawShip(Graphics g, Ship ship, boolean isOwner, boolean sunk, Set<Point> fov) {
		Graphics2D g2 = (Graphics2D) g;

		Image img;
		BufferedImage buff;
		RescaleOp op = sunk ? SUNKEN_SHIP_TRANSFORM : null;

		Direction dir = ship.getDirection();
		int maxHealth = ship.getMaxHealthPerSquare();
		int health;
		ResourceManager rm = ResourceManager.getInstance();
		List<Point> points = ship.getShipLine().getPoints();

		if (ship.getSize() == 1) {
			if (isOwner || fov.contains(ship.getPosition()) || GOD_MODE) {
				g.setColor(Color.black);
				g.fillRect(ship.getPosition().x * Constants.TILE_SIZE, 
						ship.getPosition().y * Constants.TILE_SIZE,
						Constants.TILE_SIZE,
						Constants.TILE_SIZE);
			}
			return;
		}	
		
		Point head = points.remove(points.size() - 1);
		Point tail = points.remove(0);
		
		// Render ship head
		if (isOwner || fov.contains(head) || GOD_MODE) {
			health = ship.getHealth(ship.getSize()-1);
			int x = (int)head.x * Constants.TILE_SIZE;
			int y = (int)head.y * Constants.TILE_SIZE;

			// Get image as buffered image
			img = rm.getShipHeadImage(dir, health, maxHealth, isOwner);
			buff = ResourceManager.toBufferedImage(img, this);
			g2.drawImage(buff, op, x, y);		
		}

		// Render ship tail
		if (isOwner || fov.contains(tail) || GOD_MODE) {
			health = ship.getHealth(0);
			int x = (int)tail.x * Constants.TILE_SIZE;
			int y = (int)tail.y * Constants.TILE_SIZE;
			
			// Get image as buffered image
			img = rm.getShipTailImage(dir, health, maxHealth, isOwner);
			buff = ResourceManager.toBufferedImage(img, this);
			g2.drawImage(buff, op, x, y);
		}

		// Render ship body
		for (int i = 0; i < points.size(); i++) {
			Point body = points.get(i);
			if (isOwner || fov.contains(body) || GOD_MODE) {
				health = ship.getHealth(i+1);
				int x = (int)body.x * Constants.TILE_SIZE;
				int y = (int)body.y * Constants.TILE_SIZE;

				img = rm.getShipBodyImage(dir, health, maxHealth);
				buff = ResourceManager.toBufferedImage(img, this);
				g2.drawImage(buff, op, x, y);
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
		Point p = new Point(x, y);
		Image baseImage = ResourceManager.getInstance().getBaseImage(y, game.getField().isBaseDestroyed(p));
		g.drawImage(baseImage, x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
	}

	public void drawWater(Graphics g, int x, int y) {
		g.drawImage(ResourceManager.getInstance().getWaterImage(), x
				* Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
	}
	
	private void drawGameEvents(Graphics g, Set<Point> fov) {
		Graphics2D g2 = (Graphics2D) g;

		Image image = ResourceManager.getInstance().getEventImage();
		BufferedImage buff = ResourceManager.toBufferedImage(image, this);

		for (GameEvent e : events) {
			int x = e.getPoint().x * Constants.TILE_SIZE;
			int y = e.getPoint().y * Constants.TILE_SIZE;

			g2.drawImage(buff, x, y, null);
			
			if (fov.contains(e.getPoint()) && !e.getPlayedSound()) {
				SoundManager sm = SoundManager.getInstance();
				e.setPlayedSound(true);
				
				if (e.getEffect() == Effect.HIT_WATER) {
					sm.play("hit-water");
				} else {
					if (e.getCause() == Cause.CANNON) {
						sm.play("cannon");
					}
				}
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


	private void drawLongRangeRadarOutlines(Graphics g) {
		g.setColor(Constants.LONG_RANGE_RADAR_BORDER);
		for (Ship s : game.getShips()) {
			if (!s.isSunk() && s.getOwner().equals(isP1 ? game.getP1() : game.getP2()) || GOD_MODE) {
				if (s.hasLongRangeRadar() && s.isLongRangeRadarEnabled()) {
					// Render outline around rectangle for long range radar	
					Rectangle r = s.getActiveRadar().getRectangle(s);
					g.drawRect(r.x * Constants.TILE_SIZE, r.y * Constants.TILE_SIZE,
							(int)r.getWidth() * Constants.TILE_SIZE, (int)r.getHeight() * Constants.TILE_SIZE);
				}
			}
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		// Only update the field panel if a turn actually passed
		if (o instanceof GameManager) {
			recalculateFieldsOfVision();
			if (arg instanceof List) {
				events = (List<GameEvent>)arg;
				eventContext.setEvent(null);
			}
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
				// If the mouse, see if we have any game events using that position
				Point p = new Point(newX, newY);
				GameEvent foundEvent = null;
				for (GameEvent event : events) {
					if (event.getPoint().equals(p)) {
						foundEvent = event;
						break;
					}
				}
				eventContext.setEvent(foundEvent);
				
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
		repaint();
		return true;
	}
}