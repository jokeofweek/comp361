package comp361.shared.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comp361.client.data.event.Cause;
import comp361.client.data.event.Effect;
import comp361.client.data.event.GameEvent;
import comp361.shared.data.range.CenterRange;
import comp361.shared.data.range.Range;
import comp361.shared.data.range.TailRange;

public class Ship {

	public static final Ship CRUISER_TEMPLATE = new Ship("Cruiser", 5, 10, 0,
			ArmorType.HEAVY, false, false, false, false, true, false, false,
			false, new TailRange(10, 3), null, new CenterRange(15, 11));
	public static final Ship DESTROYER_TEMPLATE = new Ship("Destroyer", 4, 8,
			0, ArmorType.NORMAL, false, false, false, false, false, true,
			false, false, new TailRange(8, 3), null, new CenterRange(12, 9));
	// TODO: Fix torpedo cannon range
	public static final Ship TORPEDO_TEMPLATE = new Ship("Torpedo", 3, 9, 0,
			ArmorType.NORMAL, true, false, false, false, false, true, false,
			false, new TailRange(6, 3), null, new CenterRange(5, 5));
	public static final Ship MINE_LAYER_TEMPLATE = new Ship("Mine Layer", 2, 6,
			5, ArmorType.HEAVY, false, true, false, false, false, true, true,
			false, new CenterRange(6, 5), null, new CenterRange(4, 5));
	public static final Ship RADAR_BOAT_TEMPLATE = new Ship("Radar Boat", 3, 3,
			0, ArmorType.NORMAL, true, false, true, false, false, false, false,
			false, new TailRange(6, 3), new TailRange(12, 3), new CenterRange(
					5, 3));
	private static final int TORPEDO_RANGE = 10;

	// This is the position of the head of the ship.
	private Point position;
	private String owner;
	private String name;
	private transient Game game;
	private int size;
	private int speed;
	private int mineCount;
	private Direction facing;
	private ArmorType armor;
	private int[] health;
	private boolean turnsOnCenter;
	private boolean isMineLayer;
	private boolean hasLongRangeRadar;
	private boolean longRangeRadarEnabled;
	private boolean hasHeavyCannon;
	private boolean hasTorpedoes;
	private boolean hasSonar;
	private boolean canTurn180;
	private Range radar;
	private Range longRadar;
	private Range cannonRange;

	public Ship() {
		// TODO Auto-generated constructor stub
	}

	public Ship(String name, int size, int speed, int mineCount,
			ArmorType armor, boolean turnsOnCenter, boolean isMineLayer,
			boolean hasLongRangeRadar, boolean longRangeRadarEnabled,
			boolean hasHeavyCannon, boolean hasTorpedoes, boolean hasSonar,
			boolean canTurn180, Range radar, Range longRadar, Range cannonRange) {
		super();
		this.name = name;
		this.size = size;
		this.speed = speed;
		this.mineCount = mineCount;
		this.armor = armor;
		this.health = new int[size];
		this.turnsOnCenter = turnsOnCenter;
		this.isMineLayer = isMineLayer;
		this.hasLongRangeRadar = hasLongRangeRadar;
		this.longRangeRadarEnabled = longRangeRadarEnabled;
		this.hasHeavyCannon = hasHeavyCannon;
		this.hasTorpedoes = hasTorpedoes;
		this.hasSonar = hasSonar;
		this.canTurn180 = canTurn180;
		this.radar = radar;
		this.longRadar = longRadar;
		this.cannonRange = cannonRange;

		Arrays.fill(health, armor.getHealthPointsPerSquare());
	}

	public Ship clone(Game game, String owner) {
		Ship s = new Ship(name, size, speed, mineCount, armor, turnsOnCenter,
				isMineLayer, hasLongRangeRadar, longRangeRadarEnabled,
				hasHeavyCannon, hasTorpedoes, hasSonar, canTurn180, radar,
				longRadar, cannonRange);
		s.owner = owner;
		s.game = game;
		return s;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the size of the ship
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * @return the game in which the ship is
	 */
	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return the owner of the ship
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * @return the speed of the ship
	 */
	public int getSpeed() {
		// We have to reduce the speed depending on damage.
		int damagedSquares = 0;
		for (int i = 0; i < health.length; i++) {
			if (health[i] == 0) {
				damagedSquares++;
			}
		}
		// Reduce speed by percentage of squares broken
		int speedReduction = (int)Math.round((((double)damagedSquares)/health.length) * this.speed);
		return this.speed - speedReduction;
	}

	/**
	 * @return the direction the ship is facing
	 */
	public Direction getDirection() {
		return this.facing;
	}

	/**
	 * @param direction
	 *            the new direction the ship should face
	 */
	public void setDirection(Direction direction) {
		this.facing = direction;
	}

	/**
	 * @return the position of the head of the ship
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * @return the numbers of mines the ship is carrying
	 */
	public int getMineCount() {
		return this.mineCount;
	}

	/**
	 * @param mineCount
	 *            the new number of mines the ship carries
	 */
	public void setMineCount(int mineCount) {
		this.mineCount = mineCount;
	}

	/**
	 * @return true if the ship is a Mine Layer, false otherwise
	 */
	public boolean isMineLayer() {
		return this.isMineLayer;
	}

	/**
	 * @return true if the ship is equipped with a heavy cannon, false otherwise
	 */
	public boolean hasHeavyCannon() {
		return this.hasHeavyCannon();
	}

	/**
	 * @return true if the ship can perform 180 degrees turns, false otherwise
	 */
	public boolean canTurn180() {
		return this.canTurn180;
	}

	/**
	 * @return the set of points reachable by the ship's cannon
	 */
	public Range getCannonRange() {
		return cannonRange;
	}

	/**
	 * @param p
	 *            the target location of the cannon ball
	 * @return true if the operation succeeds, false otherwise
	 */
	public boolean fireCannon(Point p, List<GameEvent> events) {
		if (this.getCannonRange().getRectangle(this).contains(p)) {
			if (this.game.getField().getCellType(p) == CellType.MINE) {
				// destroy the mine
				this.game.getField().setCellType(p, CellType.WATER);
				// Log the event
				events.add(new GameEvent(Arrays.asList(p), 
						Cause.CANNON, Arrays.asList(Effect.MINE_DESTROYED), this));
			} else if (this.game.getField().getCellType(p) == CellType.BASE){
				// Test if the base was destroyed beforehand
				boolean alreadyDestroyed = this.game.getField().isBaseDestroyed(p);
				// damage the base
				this.game.getField().damageBase(p);

				events.add(new GameEvent(Arrays.asList(p), 
						Cause.CANNON, Arrays.asList(alreadyDestroyed ? Effect.BASE_HIT : Effect.BASE_DESTROYED), this));
			} else {
				boolean hit = false;
				for (Ship s : this.game.getShips()) { 
					if (s.pointBelongsToShip(p)) {
						s.hitWithCannon(p, this.hasHeavyCannon);
						
						// Log the event
						hit = true;
						events.add(new GameEvent(Arrays.asList(p), 
								Cause.CANNON, Arrays.asList(s.isSunk() ? Effect.SHIP_SUNK : Effect.SHIP_HIT), this));
						break;
					}
				}
				
				// If hit nothing, log that
				if (!hit) {
					events.add(new GameEvent(Arrays.asList(p), 
							Cause.CANNON, Arrays.asList(Effect.HIT_WATER), this));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param p
	 *            the target point of the enemy cannon ball
	 * @param isHeavyCannon
	 *            true if the enemy cannon ball is HEAVY, false otherwise
	 */
	public void hitWithCannon(Point p, boolean isHeavyCannon) {
		if (health[getShipLine().getPoints().indexOf(p)] > 0) {
			if (isHeavyCannon) {
				health[getShipLine().getPoints().indexOf(p)] = 0;
			} else {
				health[getShipLine().getPoints().indexOf(p)]--;
			}
		}
	}

	/**
	 * @return true if the ship is equipped with torpedoes, false otherwise
	 */
	public boolean hasTorpedoes() {
		return this.hasTorpedoes;
	}

	/**
	 * @return the trajectory of the ship's torpedoes
	 */
	public Line getTorpedoLine() {
		Point firstPoint;
		if (facing == Direction.LEFT)
			firstPoint = new Point(position.x - 1, position.y);
		else if (facing == Direction.UP)
			firstPoint = new Point(position.x, position.y - 1);
		else if (facing == Direction.RIGHT)
			firstPoint = new Point(position.x + 1, position.y);
		else
			firstPoint = new Point(position.x, position.y + 1);
		return new Line(firstPoint, facing, TORPEDO_RANGE);
	}

	/**
	 * Fires a torpedo
	 */
	public void fireTorpedo(List<GameEvent> events) {
		if (this.hasTorpedoes) {
			for (Point p : getTorpedoLine().getPoints()) {
				if (game.getField().getCellType(p) == CellType.BASE) {
					// Test if the base was destroyed beforehand
					boolean alreadyDestroyed = this.game.getField().isBaseDestroyed(p);
					// damage the base
					this.game.getField().damageBase(p);

					events.add(new GameEvent(Arrays.asList(p), 
							Cause.TORPEDO, Arrays.asList(alreadyDestroyed ? Effect.BASE_HIT : Effect.BASE_DESTROYED), this));
				} else if (game.getField().getCellType(p) == CellType.MINE) {
					// Remove the mine
					game.getField().setCellType(p, CellType.WATER);
					// Log the event
					events.add(new GameEvent(Arrays.asList(p), 
							Cause.TORPEDO, Arrays.asList(Effect.MINE_DESTROYED), this));
				} else if (game.getField().getCellType(p) == CellType.REEF) {
					// Do nothing!
					return;
				} else {
					for (Ship s : game.getShips()) {
						if (s.pointBelongsToShip(p)) {
							s.hitWithTorpedo(p, this.facing);

							// Log the event
							events.add(new GameEvent(Arrays.asList(p), 
									Cause.TORPEDO, Arrays.asList(s.isSunk() ? Effect.SHIP_SUNK : Effect.SHIP_HIT), this));
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * @param p
	 *            the target position of the enemy torpedo
	 * @param shootingDirection
	 *            the direction of the incoming torpedo
	 */
	public void hitWithTorpedo(Point p, Direction shootingDirection) {
		// damage the right square
		if (health[getShipLine().getPoints().indexOf(p)] > 0)
			health[getShipLine().getPoints().indexOf(p)]--;
		// if perpendicular, damage another adjacent square
		if (Math.abs(shootingDirection.angleBetween(facing)) == Math.PI / 2) {
			if (Math.abs(facing.angle()) == Math.PI) {
				// try to damage two potential points
				Point secondP1 = new Point(p.x, p.y + 1);
				Point secondP2 = new Point(p.x, p.y - 1);
				if (pointBelongsToShip(secondP1)
						&& health[getShipLine().getPoints().indexOf(secondP1)] > 0)
					health[getShipLine().getPoints().indexOf(secondP1)]--;
				else if (pointBelongsToShip(secondP2)
						&& health[getShipLine().getPoints().indexOf(secondP2)] > 0)
					health[getShipLine().getPoints().indexOf(secondP2)]--;
			} else {
				// try to damage two potential points
				Point secondP1 = new Point(p.x + 1, p.y);
				Point secondP2 = new Point(p.x - 1, p.y);
				if (pointBelongsToShip(secondP1)
						&& health[getShipLine().getPoints().indexOf(secondP1)] > 0)
					health[getShipLine().getPoints().indexOf(secondP1)]--;
				else if (pointBelongsToShip(secondP2)
						&& health[getShipLine().getPoints().indexOf(secondP2)] > 0)
					health[getShipLine().getPoints().indexOf(secondP2)]--;
			}
		}
	}

	/**
	 * @return returns the line segment that the ship occupies
	 */
	public Line getShipLine() {
		Point tail = new Point(position);
		if (this.facing == Direction.UP)
			tail = new Point((int) position.getX(), (int) (position.getY()
					+ size - 1));
		else if (this.facing == Direction.DOWN)
			tail = new Point((int) position.getX(), (int) (position.getY()
					- size + 1));
		else if (this.facing == Direction.LEFT)
			tail = new Point((int) position.getX() + size - 1,
					(int) position.getY());
		else if (this.facing == Direction.RIGHT)
			tail = new Point((int) position.getX() - size + 1,
					(int) position.getY());
		return new Line(tail, position);
	}

	/**
	 * Moves the ship to the position, given there are no obstacles on the path.
	 * 
	 * @param p
	 *            the new position of the ship
	 */
	public void moveShip(Point p) {
		Line trajectory = getLineTo(p);
		List<Point> points = trajectory.getPoints();
		// Remove the head.
		points.remove(0);
		
		// Get the furthest possible position
		Point endPoint = game.getFurthestPosition(this, new Line(points.get(0), points.get(points.size() - 1)));
		if (endPoint != null) {
			setPosition(endPoint);
		}
		
		// Once we've moved, explode any adjacent mines
		if (!isMineLayer()) {
			for (Point minePoint : game.getField().getAdjacentMines(endPoint)) {
				game.explodeMine(minePoint);
			}
		}
	}

	/**
	 * This sets the position of the ship's head.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @param p
	 *            the target point
	 * @return the line segment between the ship and p
	 */
	public Line getLineTo(Point p) {
		return new Line(this.position, p);
	}

	/**
	 * @return true if the ship has a long range radar, false otherwise
	 */
	public boolean hasLongRangeRadar() {
		return this.hasLongRangeRadar;
	}

	/**
	 * @return true if the toggle operation is successful, false otherwise
	 */
	public boolean toggleLongRangeRadar() {
		try {
			this.longRangeRadarEnabled = !longRangeRadarEnabled;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return true if the long-range radar is enabled, false otherwise
	 */
	public boolean isLongRangeRadarEnabled() {
		return this.longRangeRadarEnabled;
	}

	/**
	 * @return true if the ship has a sonar, false otherwise
	 */
	public boolean hasSonar() {
		return this.hasSonar;
	}

	/**
	 * @param index
	 *            index of a square in the ship
	 * @return the health of the square at index
	 */
	public int getHealth(int index) {
		return health[index];
	}

	/**
	 * @param d
	 *            the new direction of the ship
	 * @return true if the turning operation succeeds, false otherwise
	 */
	public boolean turnShip(Direction d) {
		if (this.canTurnToFace(d)) {
			Point pivot;
			this.facing = d;
			if (this.turnsOnCenter) {
				// ship turns on its center
				pivot = getShipLine().getPoints().get(this.size / 2);
				this.position = new Line(pivot, d, this.size / 2).getHead();
			} else {
				// ship turns on its tail
				pivot = getShipLine().getTail();
				this.position = new Line(pivot, d, this.size).getHead();
			}
			return true;
		}
		return false;
	}

	/**
	 * @param d
	 *            the direction the ship would face
	 * @return true if the ship can turn to the direction, false otherwise
	 */
	public boolean canTurnToFace(Direction d) {
		if (d == this.facing.opposite() && !canTurn180)
			return false;
		return !(game.hasObstacle(getPointsInTurnRadius(d)));
	}

	/**
	 * @param d
	 *            the direction the ship would face
	 * @return the list points the ship would traverse if it turned
	 */
	public List<Point> getPointsInTurnRadius(Direction d) {
		List<Point> points = new ArrayList<Point>();
		Point pivot, corner1, corner2;
		if (turnsOnCenter) {
			pivot = getShipLine().getPoints().get(this.size / 2);
			// if(d == facing.opposite())

		} else {
			pivot = getShipLine().getTail();
		}
		return points;
	}

	/**
	 * @param p
	 *            the position of the colliding object
	 * @return the position of the square on the ship colliding with p
	 */
	public Point getCollidingBlockPosition(Point p) {
		Point point = new Point();
		// TODO: implement this
		return point;
	}

	/**
	 * @param p
	 *            A point on the field
	 * @return true if the point belongs to the ship and the ship is not sunk, false otherwise
	 */
	public boolean pointBelongsToShip(Point p) {
		return getShipLine().contains(p) && !isSunk();
	}

	/**
	 * @return the active radar, whether it's the long range or the regular
	 *         range.
	 */
	public Range getActiveRadar() {
		if (longRangeRadarEnabled) {
			return longRadar;
		} else {
			return radar;
		}
	}

	/**
	 * @return a set containing all points a ship can move to.
	 */
	public Set<Point> getValidMovePoints() {

		Set<Point> points = new HashSet<Point>();

		if (this.facing == Direction.UP) {
			// The point below (behind) the ship
			int backY = this.position.y + this.size;
			Point backPoint = new Point(this.position.x, backY);
			points.add(backPoint);

			// Add all the points to the right of the ship
			int rightX = this.position.x + 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point rightPoint = new Point(rightX, p.y);
				points.add(rightPoint);
			}

			// Add all the points to the left of the ship
			int leftX = this.position.x - 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point leftPoint = new Point(leftX, p.y);
				points.add(leftPoint);
			}

			// Add all the points above(in front of) the ship
			Point movementCap = new Point(this.position.x, this.position.y
					- this.getSpeed());
			for (Point p : getLineTo(movementCap).getPoints()) {
				if (this.getShipLine().contains(p)) {
					continue;
				}

				else {
					points.add(p);
				}
			}
		}

		else if (this.facing == Direction.DOWN) {
			// Add the point above (behind) the ship
			int backY = this.position.y - this.size;
			Point backPoint = new Point(this.position.x, backY);
			points.add(backPoint);

			// Add all the points to the right of the ship
			int rightX = this.position.x + 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point rightPoint = new Point(rightX, p.y);
				points.add(rightPoint);
			}

			// Add all the points to the left of the ship
			int leftX = this.position.x - 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point leftPoint = new Point(leftX, p.y);
				points.add(leftPoint);
			}

			// Add all the points below(in front of) the ship
			Point movementCap = new Point(this.position.x, this.position.y
					+ this.getSpeed());
			for (Point p : getLineTo(movementCap).getPoints()) {
				if (this.getShipLine().contains(p)) {
					continue;
				}

				else {
					points.add(p);
				}
			}
		}

		else if (this.facing == Direction.LEFT) {
			// Add the point to the right of (behind) the ship
			int backX = this.position.x + this.size;
			Point backPoint = new Point(backX, this.position.y);
			points.add(backPoint);

			// Add all the points right above the ship
			int topY = this.position.y - 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point topPoint = new Point(p.x, topY);
				points.add(topPoint);
			}

			// Add all the points right below the ship
			int bottomY = this.position.y + 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point bottomPoint = new Point(p.x, bottomY);
				points.add(bottomPoint);
			}

			// Add all the points in front of the ship
			Point movementCap = new Point(this.position.x - this.getSpeed(),
					this.position.y);
			for (Point p : getLineTo(movementCap).getPoints()) {
				if (this.getShipLine().contains(p)) {
					continue;
				}

				else {
					points.add(p);
				}
			}
		}

		else {
			// Add the points to the left of (behind) the ship
			int backX = this.position.x - this.size;
			Point backPoint = new Point(backX, this.position.y);
			points.add(backPoint);

			// Add all the points right above the ship
			int topY = this.position.y - 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point topPoint = new Point(p.x, topY);
				points.add(topPoint);
			}

			// Add all the points below the ship
			int bottomY = this.position.y + 1;
			for (Point p : this.getShipLine().getPoints()) {
				Point bottomPoint = new Point(p.x, bottomY);
				points.add(bottomPoint);
			}

			// Add all the points in front of the ship
			Point movementCap = new Point(this.position.x + this.getSpeed(),
					this.position.y);
			for (Point p : getLineTo(movementCap).getPoints()) {
				if (this.getShipLine().contains(p)) {
					continue;
				}

				else {
					points.add(p);
				}
			}
		}

		return game.getField().filterInBoundPoints(points);
	}

	/**
	 * @return the max health of each square of the ship
	 */
	public int getMaxHealthPerSquare() {
		return armor.getHealthPointsPerSquare();
	}

	/**
	 * Repairs one block of the ship, starting from the front and ending at the
	 * back.
	 */
	public void repair() {
		for (int i = health.length - 1; i >= 0; i--) {
			if (health[i] < armor.getHealthPointsPerSquare()) {
				health[i] = armor.getHealthPointsPerSquare();
				return;
			}
		}
	}

	/**
	 * @return a set containing the points directly adjacent to the ship.
	 */
	private Set<Point> getSurroundingPoints() {
		Set<Point> points = new HashSet<>();

		// Get the points all along the ship line
		List<Point> linePoints = getShipLine().getPoints();

		// Add the head and tail based on direction
		if (facing == Direction.LEFT) {
			points.add(new Point(linePoints.get(0).x + 1, linePoints.get(0).y));
			points.add(new Point(position.x - 1, position.y));
		} else if (facing == Direction.RIGHT) {
			points.add(new Point(linePoints.get(0).x - 1, linePoints.get(0).y));
			points.add(new Point(position.x + 1, position.y));
		} else if (facing == Direction.UP) {
			points.add(new Point(linePoints.get(0).x, linePoints.get(0).y + 1));
			points.add(new Point(position.x, position.y - 1));
		} else {
			points.add(new Point(linePoints.get(0).x, linePoints.get(0).y - 1));
			points.add(new Point(position.x, position.y + 1));
		}

		// Add points on both sides of body
		int yOffset = (facing == Direction.LEFT || facing == Direction.RIGHT) ? 1
				: 0;
		int xOffset = (facing == Direction.UP || facing == Direction.DOWN) ? 1
				: 0;

		for (Point p : linePoints) {
			points.add(new Point(p.x + (xOffset * 1), p.y + (yOffset * 1)));
			points.add(new Point(p.x + (xOffset * -1), p.y + (yOffset * -1)));
		}

		return points;
	}

	/**
	 * @return the set of all adjacent points where a mine pickup action can be performed.
	 */
	public Set<Point> getValidMinePickupPoints() {
		Set<Point> points = new HashSet<>();
		for (Point p : getSurroundingPoints()) {
			if (game.getField().getCellType(p) == CellType.MINE) {
				points.add(p);
			}
		}
		
		return game.getField().filterInBoundPoints(points);
	}

	/**
	 * @return the set of all points where a mine can be dropped.
	 */
	public Set<Point> getValidMineDropPoints() {
		Set<Point> points = new HashSet<>();
		for (Point p : getSurroundingPoints()) {
			// Can only drop in water
			if (game.getField().getCellType(p) == CellType.WATER) {
				boolean isValid = true;
				for (Point adjP : game.getField().getAdjacentPoints(p)) {
					// Adjacent point must also be water
					if (game.getField().getCellType(adjP) != CellType.WATER) {
						isValid = false;
						break;
					}

					// Ensure no ship on any adjacent point
					for (Ship s : game.getShips()) {
						if (s != this && s.pointBelongsToShip(adjP)) {
							isValid = false;
						}
					}
				}
				
				if (isValid) {
					points.add(p);
				}
			}
		}
		return game.getField().filterInBoundPoints(points);
	}
	
	/**
	 * @return true if a ship is sunk (eg. no squares have health)
	 */
	public boolean isSunk() {
		for (int i = 0; i < health.length; i++) {
			if (health[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Apply mine damage to a square of the ship.
	 * @param p The ship point to be damaged.
	 */
	public void doMineDamage(Point p) {
		// Iterate through the ship line, finding the point to damage.
		int i = 0;
		for (Point shipP : getShipLine().getPoints()) {
			if (shipP.equals(p)) {
				health[i] = 0;
				return;
			} else {
				i++;
			}
		}
	}
}
