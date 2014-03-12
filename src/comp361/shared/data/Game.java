package comp361.shared.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comp361.client.data.event.GameEvent;
import comp361.client.ui.setup.CoralReefGenerator;
import comp361.shared.Constants;
import comp361.shared.packets.shared.GameMovePacket;

public class Game {
	private String p1;
	private String p2;
	private Field field;
	private List<Ship> ships;

	public Game() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a new game
	 * 
	 * @param p1
	 *            The first player to play the game
	 * @param p2
	 *            The second player to play the game
	 * @param seed
	 *            The seed for the random field.
	 */
	public Game(String p1, String p2, long seed) {
		this.p1 = p1;
		this.p2 = p2;
		this.field = new Field(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		this.ships = new ArrayList<>();

		// Setup the field with the base
		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			field.setCellType(new Point(0, Constants.BASE_Y_OFFSET + y),
					CellType.BASE);
			field.setCellType(new Point(Constants.MAP_WIDTH - 1,
					Constants.BASE_Y_OFFSET + y), CellType.BASE);
		}

		// Copy over the reefs
		CoralReefGenerator generator = new CoralReefGenerator();
		generator.regenerateReef(seed);
		for (int x = 0; x < Constants.CORAL_WIDTH; x++) {
			for (int y = 0; y < Constants.CORAL_HEIGHT; y++) {
				if (generator.getReef()[x][y]) {
					field.setCellType(new Point(Constants.CORAL_X_OFFSET + x,
							Constants.CORAL_Y_OFFSET + y), CellType.REEF);
				}
			}
		}
		
		

		for (int i = 0; i < Ship.DEFAULT_INVENTORY.length; i++) {
			Ship s = Ship.DEFAULT_INVENTORY[i].clone(this, p1);
			placeShipAt(s, i);
			Ship s2 = Ship.DEFAULT_INVENTORY[i].clone(this, p2);
			placeShipAt(s2, i);
			ships.add(s);
			ships.add(s2);
		}
	}

	/**
	 * @return the first player
	 */
	public String getP1() {
		return p1;
	}

	/**
	 * @return the second player
	 */
	public String getP2() {
		return p2;
	}

	/**
	 * @return the field of the game
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @return the ships in the game
	 */
	public List<Ship> getShips() {
		return this.ships;
	}

	/**
	 * Gets the furthest position along which a ship can move.
	 * @param ship THe ship to move.
	 * @param line The line along which it wants to move.
	 * @return The point up to which the ship can move to, or null if it can't move.
	 */
	public Point getFurthestPosition(Ship ship, Line line) {
		
		// Iterate through all points, trying to find the furthest possible move
		Point last = null;
		for (Point p : line.getPoints()) {
			// If the point isn't water, then can't move past it.
			if (field.getCellType(p) != CellType.WATER) {
				return last;
			} else {
				// Check if there is a ship at that point
				for (Ship s : ships) {
					if (s.pointBelongsToShip(p)) {
						return last;
					}
				}
			}
			
			last = p;
			// If we can move to it, but we aren't a mine layer and there are adjacnet mines, we need to explode!
			if (!ship.isMineLayer() && !field.getAdjacentMines(p).isEmpty()) {
				return last;
			}
		}
		return last;
	}

	/**
	 * @param p
	 *            The player to which the base belongs
	 * @return The set of points visible from the player's base
	 */
	public Set<Point> getPointsVisibleFromBase(String p) {
		HashSet<Point> points = new HashSet<Point>();

		// This is the column where the base is
		int baseX = 0;
		// This lets us pick points either to the left of the base or to the
		// right of it.
		int offsetX = 0;
		if (getP1().equals(p)) {
			baseX = 0;
			offsetX = 1;
		} else if (getP2().equals(p)) {
			baseX = Constants.MAP_WIDTH - 1;
			offsetX = -1;
		} else {
			// Wtf?
			throw new IllegalArgumentException("Not a valid player.");
		}

		// Add the points above and below the base
		points.add(new Point(baseX, Constants.BASE_Y_OFFSET - 1));
		points.add(new Point(baseX + offsetX, Constants.BASE_Y_OFFSET - 1));
		points.add(new Point(baseX, Constants.BASE_Y_OFFSET
				+ Constants.BASE_HEIGHT));
		points.add(new Point(baseX + offsetX, Constants.BASE_Y_OFFSET
				+ Constants.BASE_HEIGHT));

		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			// Add the base itself as well as the point to the left/right of it.
			points.add(new Point(baseX, y + Constants.BASE_Y_OFFSET));
			points.add(new Point(baseX + offsetX, y + Constants.BASE_Y_OFFSET));
		}

		return points;
	}

	/**
	 * @param point
	 *            A point on the map
	 * @param player
	 *            The player
	 * @return Returns the visibility of the point if it is visible to the
	 *         player, null otherwise
	 */
	public VisibilityType isVisible(Point point, String player) {
		// TODO: implement this
		return VisibilityType.WATER;
	}

	/**
	 * @param p1
	 *            Player to set as Player 1
	 * @param p2
	 *            Player to set as Player 2
	 */
	public void setPlayers(String p1, String p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * @param s
	 *            The ship
	 * @param index
	 *            The index
	 * @return
	 */
	public boolean isIndexEmpty(Ship s, int index) {
		// TODO: implement this
		return false;
	}

	/**
	 * This obtains the direction that the player should be facing at the beginning.
	 * @param p The player.
	 * @return The direciton ships will start facing.
	 */
	public Direction getInitialDirection(String p) {
		if (p.equals(p1)) {
			return Direction.RIGHT;
		} else if (p.equals(p2)) {
			return Direction.LEFT;
		} else {
			throw new IllegalArgumentException("Not a valid player.");
		}
	}

	/**
	 * Places the ship along the base
	 * 
	 * @param s
	 *            The ship to be placed
	 * @param index
	 *            The index of the position along the base
	 */
	public void placeShipAt(Ship s, int index) {
		s.setDirection(getInitialDirection(s.getOwner()));
		s.setPosition(field.getIndexPosition(s, index));
	}

	/**
	 * Explodes the mine at position p
	 * 
	 * @param minePoint
	 *            The position of the mine to explode
	 */
	public void explodeMine(Point minePoint) {
		// Get the adjacent points
		for (Point p : field.getAdjacentPoints(minePoint)) {
			// Look through each ship, testing if they touch the mine
			// and if so destroying the square that touched it.
			for (Ship s : ships) {
				if (s.pointBelongsToShip(p)) {
					s.doMineDamage(p);
					break;
				}
			}
		}
		// Update the field
		field.setCellType(minePoint, CellType.WATER);
	}

	/**
	 * @param s
	 *            The ship to damage
	 * @param collidingPoint
	 *            The point of the ship colliding with the mine
	 */
	public void applyRotationMineDamage(Ship s, Point collidingPoint) {
		// TODO: implement this
	}

	/**
	 * @param points
	 *            A list of points on the map
	 * @return true if an obstacle exists at any of the given points, false
	 *         otherwise
	 */
	public boolean hasObstacle(List<Point> points) {
		for (Point p : points) {
			if (field.getCellType(p) == CellType.MINE
					|| field.getCellType(p) == CellType.REEF
					|| field.getCellType(p) == CellType.BASE)
				return true;
			for (Ship s : ships)
				if (s.pointBelongsToShip(p))
					return true;
		}
		return false;
	}

	/**
	 * @param points
	 *            A list of points on the map
	 * @return The type of the closest obstacle
	 */
	public ObstacleType getClosestObstacleType(List<Point> points) {
		for (Point p : points) {
			for (Ship s : ships)
				if (s.pointBelongsToShip(p))
					return ObstacleType.SHIP;
			if (field.getCellType(p) == CellType.BASE) {
				return ObstacleType.BASE;
			} else if (field.getCellType(p) == CellType.MINE) {
				return ObstacleType.MINE;
			} else if (field.getCellType(p) == CellType.REEF) {
				return ObstacleType.REEF;
			}
		}
		return null;
	}

	/**
	 * @param points
	 *            A list of points on the map
	 * @return The position of the closest obstacle
	 */
	public Point getClosestObstaclePosition(List<Point> points) {
		for (Point p : points) {
			for (Ship s : ships)
				if (s.pointBelongsToShip(p))
					return p;
			if (field.getCellType(p) == CellType.MINE
					|| field.getCellType(p) == CellType.REEF
					|| field.getCellType(p) == CellType.BASE)
				return p;
		}
		return null;
	}

	/**
	 * @param p
	 *            A player in the game
	 * @return A list of the ships of the specified player
	 */
	public List<Ship> getPlayerShips(String p) {
		List<Ship> playerShips = new ArrayList<Ship>();
		for (Ship s : ships)
			if (s.getOwner().equals(p))
				playerShips.add(s);
		return playerShips;
	}

	/**
	 * Gets the id of a ship for a {@link GameMovePacket}.
	 * 
	 * @param s
	 * @return
	 */
	public int getShipId(Ship s) {
		return ships.indexOf(s);
	}

	/**
	 * Get the set of points representing the base for a player.
	 * 
	 * @param player
	 *            The player's name
	 * @return The set of points for that player's base.
	 */
	public Set<Point> getBasePoints(String player) {
		Set<Point> points = new HashSet<>();

		if (player.equals(p1)) {
			for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
				points.add(new Point(0, y + Constants.BASE_Y_OFFSET));
			}
		} else if (player.equals(p2)) {
			for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
				points.add(new Point(Constants.MAP_WIDTH - 1, y
						+ Constants.BASE_Y_OFFSET));
			}
		} else {
			throw new IllegalArgumentException("Invalid player.");
		}

		return points;
	}

	/**
	 * Tests if at least one part of the ship is touching the owner's base, and
	 * thus it can be repaired.
	 * 
	 * @param s
	 *            The ship in question.
	 * @return True if the ship is touching the base.
	 */
	public boolean canRepair(Ship s) {
		// Get the base points
		Set<Point> basePoints = getBasePoints(s.getOwner());

		// Test if the ship is touching the base.
		for (Point p : s.getShipLine().getPoints()) {
			for (Point bp : basePoints) {
				// TODO: Handle a base being damaged.
				if (Math.abs(p.x - bp.x) + Math.abs(p.y - bp.y) <= 1) {
					return true;
				}
			}
		}
		return false;
	}
	

	public void applyMove(GameMovePacket packet) {
		Ship ship = ships.get(packet.ship);
		
		List<GameEvent> events = new ArrayList<>();

		if (packet.moveType == MoveType.MOVE) {
			ship.moveShip(packet.contextPoint);
		} else if (packet.moveType == MoveType.CANNON) {
			ship.fireCannon(packet.contextPoint, events);
		} else if (packet.moveType == MoveType.TORPEDO) {
			ship.fireTorpedo(events);
		} else if (packet.moveType == MoveType.REPAIR) {
			ship.repair();
		} else if (packet.moveType == MoveType.PICKUP_MINE) {
			ship.setMineCount(ship.getMineCount() + 1);
			ship.getGame().getField().setCellType(packet.contextPoint, CellType.WATER);
		} else if (packet.moveType == MoveType.DROP_MINE) {
			ship.setMineCount(ship.getMineCount() - 1);
			ship.getGame().getField().setCellType(packet.contextPoint, CellType.MINE);
		}

		System.out.println("Move applied. Events: ");
		for (GameEvent e : events) {
			System.out.println(e.getPoint() + " - " + e.getCause() + " - " + e.getEffect());
		}
		System.out.println();
	}
}
