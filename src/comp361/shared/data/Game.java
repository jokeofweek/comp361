package comp361.shared.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import comp361.client.ui.setup.CoralReefGenerator;
import comp361.shared.Constants;

public class Game {
	private String p1;
	private String p2;
	private Field field;
	private List<Ship> ships;

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
	 * @param ship
	 * @param line
	 * @return
	 */
	public Point getFurthestPosition(Ship ship, Line line) {
		// TODO: implement this
		return null;
	}

	/**
	 * @param p
	 *            The player to which the base belongs
	 * @return The set of points visible from the player's base
	 */
	public Set<Point> getPointsVisibleFromBase(String p) {
		HashSet<Point> points = new HashSet<Point>();
		// TODO: implement this
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
	 * @param p
	 * @return
	 */
	public Direction getInitialDirection(String p) {
		// TODO: implement this
		return Direction.RIGHT;
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
		// TODO: implement this
	}

	/**
	 * Explodes the mine at position p
	 * 
	 * @param p
	 *            The position of the mine to explode
	 */
	public void explodeMine(Point p) {
		// TODO: implement this
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
			if (field.getCellType(p) == CellType.BASE)
				return ObstacleType.BASE;
			if (field.getCellType(p) == CellType.MINE)
				return ObstacleType.MINE;
			if (field.getCellType(p) == CellType.REEF)
				;
			return ObstacleType.REEF;
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
}
