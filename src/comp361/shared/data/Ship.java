package comp361.shared.data;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import comp361.client.data.event.Cause;
import comp361.client.data.event.Effect;
import comp361.client.data.event.GameEvent;
import comp361.shared.data.range.BeforeTailRange;
import comp361.shared.data.range.CenterRange;
import comp361.shared.data.range.Range;
import comp361.shared.data.range.TailRange;

public class Ship {

	public static final Ship CRUISER_TEMPLATE = new Ship("Cruiser", 5, 10, 0,
			ArmorType.HEAVY, CannonType.HEAVY, false, false, false, false, false, false,
			false, false, new BeforeTailRange(10, 3), null, new CenterRange(15, 11));
	public static final Ship DESTROYER_TEMPLATE = new Ship("Destroyer", 4, 8,
			0, ArmorType.NORMAL, CannonType.NORMAL, false, false, false, false, true,
			false, false, false, new BeforeTailRange(8, 3), null, new CenterRange(12, 9));
	public static final Ship TORPEDO_TEMPLATE = new Ship(	"Torpedo Boat", 3, 9, 0,
			ArmorType.NORMAL, CannonType.NORMAL, true, false, false, false, true, false,
			true, false, new BeforeTailRange(6, 3), null, new TailRange(5, 5));
	public static final Ship MINE_LAYER_TEMPLATE = new Ship("Mine Layer", 2, 6,
			5, ArmorType.HEAVY, CannonType.NORMAL, false, true, false, false, true, true,
			false, false, new CenterRange(6, 5), null, new CenterRange(4, 5));
	public static final Ship RADAR_BOAT_TEMPLATE = new Ship("Radar Boat", 3, 3,
			0, ArmorType.NORMAL, CannonType.NORMAL, true, false, true, false,  false, false,
			true, false, new BeforeTailRange(6, 3), new BeforeTailRange(12, 3), new CenterRange(
					5, 3));
	public static final Ship KAMIKAZE_BOAT_TEMPLATE = new Ship("Kamikaze Boat", 1, 2,
			0, ArmorType.HEAVY, CannonType.NONE, false, false, false, false,  false, false,
			false, true, new CenterRange(5, 5), null, null);

	private static final Ship[] DEFAULT_INVENTORY = { Ship.CRUISER_TEMPLATE, Ship.CRUISER_TEMPLATE,
		Ship.DESTROYER_TEMPLATE, Ship.DESTROYER_TEMPLATE,
		Ship.DESTROYER_TEMPLATE, Ship.TORPEDO_TEMPLATE,
		Ship.TORPEDO_TEMPLATE, Ship.MINE_LAYER_TEMPLATE,
		Ship.MINE_LAYER_TEMPLATE, Ship.RADAR_BOAT_TEMPLATE,
		Ship.KAMIKAZE_BOAT_TEMPLATE };
	private static final Ship[] ACCELERATED_INVENTORY = { Ship.TORPEDO_TEMPLATE };
	private static final Ship[] MINI_INVENTORY = { Ship.CRUISER_TEMPLATE, Ship.DESTROYER_TEMPLATE, Ship.TORPEDO_TEMPLATE };


	public static final String[] SHIP_INVENTORY_NAMES = {"Default", "1 Torpedo Ship", "3 Ships"};
	public static final Ship[][] SHIP_INVENTORIES = {Ship.DEFAULT_INVENTORY, Ship.ACCELERATED_INVENTORY, Ship.MINI_INVENTORY};


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
	private CannonType cannonType;
	private boolean hasTorpedoes;
	private boolean hasSonar;
	private boolean canTurn180;
	private boolean canKamikaze;
	private Range radar;
	private Range longRadar;
	private Range cannonRange;

	public Ship() {
		// TODO Auto-generated constructor stub
	}

	public Ship(String name, int size, int speed, int mineCount,
			ArmorType armor, CannonType cannonType, boolean turnsOnCenter, boolean isMineLayer,
			boolean hasLongRangeRadar, boolean longRangeRadarEnabled,
			boolean hasTorpedoes, boolean hasSonar, boolean canTurn180, boolean canKamikaze,
			Range radar, Range longRadar, Range cannonRange) {
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
		this.cannonType = cannonType;
		this.hasTorpedoes = hasTorpedoes;
		this.hasSonar = hasSonar;
		this.canTurn180 = canTurn180;
		this.radar = radar;
		this.longRadar = longRadar;
		this.cannonRange = cannonRange;
		this.canKamikaze = canKamikaze;
		Arrays.fill(health, armor.getHealthPointsPerSquare());
	}

	public Ship clone(Game game, String owner) {
		Ship s = new Ship(name, size, speed, mineCount, armor, cannonType, turnsOnCenter,
				isMineLayer, hasLongRangeRadar, longRangeRadarEnabled,
				hasTorpedoes, hasSonar, canTurn180, canKamikaze, radar,
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

	public ArmorType getArmor() {
		return armor;
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
	 * @return the range of the radar
	 */
	public Range getRadarRange()
	{
		return this.radar;
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
	 * @return the type of Cannon that the ship has.
	 */
	public CannonType getCannonType() {
		return cannonType;
	}

	/**
	 * @return true if the ship can perform 180 degrees turns, false otherwise
	 */
	public boolean canTurn180() {
		return this.canTurn180;
	}

	/**
	 * @return true if the ship can kamikaze.
	 */
	public boolean canKamikaze() {
		return this.canKamikaze;
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
				events.add(new GameEvent(p, Cause.CANNON, Effect.MINE_DESTROYED, null));
			} else if (this.game.getField().getCellType(p) == CellType.BASE){
				// Test if the base was destroyed beforehand
				boolean alreadyDestroyed = this.game.getField().isBaseDestroyed(p);
				// damage the base
				this.game.getField().damageBase(p);

				events.add(new GameEvent(p, Cause.CANNON, alreadyDestroyed ? Effect.BASE_HIT : Effect.BASE_DESTROYED, null));
			} else {
				boolean hit = false;
				for (Ship s : this.game.getShips()) {
					if (s.pointBelongsToShip(p)) {
						s.hitWithCannon(p, this.cannonType);

						// Log the event
						hit = true;
						events.add(new GameEvent(p,
								Cause.CANNON, s.isSunk() ? Effect.SHIP_SUNK : Effect.SHIP_HIT, s));
						break;
					}
				}

				// If hit nothing, log that
				if (!hit) {
					events.add(new GameEvent(p, Cause.CANNON, Effect.HIT_WATER, this));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param p
	 *            the target point of the enemy cannon ball
	 * @param cannonType the type of the cannon.
	 */
	public void hitWithCannon(Point p, CannonType cannonType) {
		if (health[getShipLine().getPoints().indexOf(p)] > 0) {
			if (cannonType == CannonType.HEAVY) {
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
			for (Point p : game.getField().filterInBoundPoints(getTorpedoLine().getPoints())) {
				if (game.getField().getCellType(p) == CellType.BASE) {
					// Test if the base was destroyed beforehand
					boolean alreadyDestroyed = this.game.getField().isBaseDestroyed(p);
					// damage the base
					this.game.getField().damageBase(p);

					events.add(new GameEvent(p,
							Cause.TORPEDO, (alreadyDestroyed ? Effect.BASE_HIT : Effect.BASE_DESTROYED), null));
					return;
				} else if (game.getField().getCellType(p) == CellType.MINE) {
					// Remove the mine
					game.getField().setCellType(p, CellType.WATER);
					// Log the event
					events.add(new GameEvent(p,
							Cause.TORPEDO, Effect.MINE_DESTROYED, null));
					return;
				} else if (game.getField().getCellType(p) == CellType.REEF) {
					// Log the event
					events.add(new GameEvent(p, Cause.TORPEDO, Effect.HIT_WATER, null));
					return;
				} else {
					for (Ship s : game.getShips()) {
						if (s.pointBelongsToShip(p)) {
							s.hitWithTorpedo(p, this.facing, events);
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
	public void hitWithTorpedo(Point p, Direction shootingDirection, List<GameEvent> events) {
		List<Point> damagedPoints = new ArrayList<Point>();
		int startIndex = getShipLine().getPoints().indexOf(p);
		// damage the right square
		if (health[startIndex] > 0) {
			health[startIndex]--;
		}
		damagedPoints.add(p);
		// if perpendicular, damage another adjacent square
		if (shootingDirection.isPerpendicularTo(facing)) {
			// Build a list of the two adjacent points
			List<Integer> indices = Arrays.asList(startIndex - 1, startIndex + 1);
			for (int i = 0; i < indices.size(); i++) {
				if (indices.get(i) >= 0 && indices.get(i) < health.length) {
					// If the square still has some health, pick it for damage
					if (health[indices.get(i)] > 0) {
						health[indices.get(i)]--;
						damagedPoints.add(getShipLine().getPoints().get(indices.get(i)));
						break;
					// If it's the last adjacent square, then mark it as hit anyways even if it's damaged
					} else if (i == indices.size() - 1) {
						damagedPoints.add(getShipLine().getPoints().get(indices.get(i)));
					}
				}
			}
		}

		// Log events
		for (Point point : damagedPoints) {
			// Log the event
			events.add(new GameEvent(point, Cause.TORPEDO, isSunk() ? Effect.SHIP_SUNK : Effect.SHIP_HIT, this));
		}
	}

	/**
	 * @return returns the line segment that the ship occupies
	 */
	public Line getShipLine() {
		Point tail = new Point(position);
		if (this.facing == Direction.UP)
			tail = new Point(position.x, position.y+size-1);
		else if (this.facing == Direction.DOWN)
			tail = new Point(position.x, position.y-size+1);
		else if (this.facing == Direction.LEFT)
			tail = new Point(position.x+size-1, position.y);
		else if (this.facing == Direction.RIGHT)
			tail = new Point(position.x-size+1, position.y);
		return new Line(tail, position);
	}

	/**
	 * @param p the point to shift to
	 * @return if the point requires a shift or a straightforward move
	 */
	public Point shiftShip(Point p)
	{
		//Shift backwards (Left)
		if(facing == Direction.RIGHT){
			if(p.x < this.position.x && p.y == this.position.y){
				Point newPosition = new Point(this.position.x - 1, this.position.y);
				Point collisionCheckPosition = new Point(this.position.x - this.size, this.position.y);

				for(Ship ship : getGame().getShips()){
					if(ship.pointBelongsToShip(collisionCheckPosition)){
						return collisionCheckPosition;
					}
				}

				if(getGame().getField().getCellType(collisionCheckPosition) == CellType.WATER){
					setPosition(newPosition);
					return null;
				}

				return collisionCheckPosition;
			}

			//Shift up
			else if(p.y < this.position.y){
				Point newPosition = new Point(this.position.x, this.position.y - 1);

				Line collisionLine = new Line(new Point(this.position.x - this.size + 1, this.position.y - 1), new Point(this.position.x, this.position.y - 1));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){

					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}

					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			//Shift down
			else if(p.y > this.position.y){
				Point newPosition = new Point(this.position.x, this.position.y + 1);

				Line collisionLine = new Line(new Point(this.position.x - this.size + 1, this.position.y + 1), new Point(this.position.x, this.position.y + 1));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){

					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}

					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}

			}

			else
			{
				return null;
			}
		}

		else if(facing == Direction.LEFT){
			//Shift Backwards (Right)
			if(p.x > this.position.x && p.y == this.position.y){
				Point newPosition = new Point(this.position.x + 1, this.position.y);
				Point collisionCheckPosition = new Point(this.position.x + this.size, this.position.y);

				for(Ship ship : getGame().getShips()){
					if(ship.pointBelongsToShip(collisionCheckPosition)){
						return collisionCheckPosition;
					}
				}

				if(getGame().getField().getCellType(collisionCheckPosition) == CellType.WATER){
					setPosition(newPosition);
					return null;
				}
				return collisionCheckPosition;
			}

			//Shift up
			else if(p.y < this.position.y){
				Point newPosition = new Point(this.position.x, this.position.y - 1);

				Line collisionLine = new Line(new Point(this.position.x + this.size - 1, this.position.y - 1), new Point(this.position.x, this.position.y - 1));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			//Shift down
			else if(p.y > this.position.y){
				Point newPosition = new Point(this.position.x, this.position.y + 1);

				Line collisionLine = new Line(new Point(this.position.x + this.size - 1, this.position.y + 1), new Point(this.position.x, this.position.y + 1));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			else
			{
				return null;
			}
		}

		//Shift backwards (down)
		else if(facing == Direction.UP){
			if(p.y > this.position.y && p.x == this.position.x){
				Point newPosition = new Point(this.position.x, this.position.y + 1);
				Point collisionCheckPosition = new Point(this.position.x, this.position.y + this.size);

				for(Ship ship : getGame().getShips()){
					if(ship.pointBelongsToShip(collisionCheckPosition)){
						return collisionCheckPosition;
					}
				}

				if(getGame().getField().getCellType(collisionCheckPosition) == CellType.WATER){
					setPosition(newPosition);
					return null;
				}

				return collisionCheckPosition;
			}

			//Shift left
			else if(p.x < this.position.x){
				Point newPosition = new Point(this.position.x - 1, this.position.y);

				Line collisionLine = new Line(new Point(this.position.x - 1, this.position.y + this.size - 1), new Point(this.position.x - 1, this.position.y));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			//Shift right
			else if(p.x > this.position.x){
				Point newPosition = new Point(this.position.x + 1, this.position.y);

				Line collisionLine = new Line(new Point(this.position.x + 1, this.position.y + this.size - 1), new Point(this.position.x + 1, this.position.y));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}


			else
			{
				return null;
			}
		}

		else{ // Direction = DOWN
			//Shift backwards (up)
			if(p.y < this.position.y && p.x == this.position.x){
				Point newPosition = new Point(this.position.x, this.position.y - 1);
				Point collisionCheckPosition = new Point(this.position.x, this.position.y - this.size);

				for(Ship ship : getGame().getShips()){
					if(ship.pointBelongsToShip(collisionCheckPosition)){
						return collisionCheckPosition;
					}
				}

				if(getGame().getField().getCellType(collisionCheckPosition) == CellType.WATER){
					setPosition(newPosition);
					return null;
				}

				return collisionCheckPosition;
			}

			//Shift left
			else if(p.x < this.position.x){
				Point newPosition = new Point(this.position.x - 1, this.position.y);

				Line collisionLine = new Line(new Point(this.position.x - 1, this.position.y - this.size + 1), new Point(this.position.x - 1, this.position.y));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			//Shift right
			else if(p.x > this.position.x){
				Point newPosition = new Point(this.position.x + 1, this.position.y);

				Line collisionLine = new Line(new Point(this.position.x + 1, this.position.y - this.size + 1), new Point(this.position.x + 1, this.position.y));
				boolean collision = false;
				for(Point point : collisionLine.getPoints()){
					for(Ship ship : getGame().getShips()){
						if(ship.pointBelongsToShip(point)){
							return point;
						}
					}
					if(getGame().getField().getCellType(point) != CellType.WATER){
						collision = true;
						return point;
					}
				}

				if(!collision){
					setPosition(newPosition);
					return null;
				}
				else
				{
					return null;
				}
			}

			else
			{
				return null;
			}
		}
	}

	/**
	 * @param p The point we want to go to
	 * @return true if the point requires a shift, or false if it is right in front of the ship.
	 */
	public boolean requiresShift(Point p) {
		int xOffset = 0;
		int yOffset = 0;
		if (facing == Direction.LEFT) xOffset = -1;
		else if (facing == Direction.RIGHT) xOffset = 1;
		else if (facing == Direction.UP) yOffset = -1;
		else yOffset = 1;

		// Iterate along entire line which is in front of our ship, testing if
		// we encounter the point.
		Point cur = new Point(position.x + xOffset, position.y + yOffset);
		while (getGame().getField().inBounds(cur)) {
			if (cur.equals(p)) {
				return false;
			}
			cur.setLocation(cur.x + xOffset, cur.y + yOffset);
		}
		return true;
	}

	/**
	 * Moves the ship to the position, given there are no obstacles on the path.
	 *
	 * @param p
	 *            the new position of the ship
	 */
	public void moveShip(Point p, List<GameEvent> events) {
		Line trajectory = getLineTo(p);
		List<Point> points = trajectory.getPoints();
		// Remove the head.
		points.remove(0);

		// Get the furthest possible position
		Line l = new Line(points.get(0), points.get(points.size() - 1));
		// Test if we are moving forwards or shifting (shifts only if it's not a kamikaze boat)
		if (requiresShift(p) && !this.canKamikaze) {
			Point collisionPoint = shiftShip(p);
			if(collisionPoint != null){
				events.add(new GameEvent(collisionPoint, null, Effect.SHIP_COLLISION, null));
			}

		} else {
			Point endPoint = game.getFurthestPosition(this, l);
			
			// If there is no end point, then we instantly hit an obstacle.
			int dx = 0;
			int dy = 0;
			
			if (endPoint != null) {
				dx = endPoint.x - this.position.x;
				dy = endPoint.y - this.position.y;
				setPosition(endPoint);
			}
			// Test if we didn't make it all the way. If there was a collision, add it as an event.
			if (endPoint == null || !endPoint.equals(p)) {
				Point last = null;
				for (Point lineP : l.getPoints()) {
					if (last == endPoint || (last != null && last.equals(endPoint))) {
						// If the collision point wasn't a mine, log the event
						if (game.getField().getCellType(lineP) != CellType.WATER) {
							if (game.getField().getCellType(lineP) != CellType.MINE ||
									isMineLayer()) {
							events.add(new GameEvent(lineP, null, Effect.SHIP_COLLISION, null));
							}
						}
						// If there are any ships at the the point, log the event
						for (Ship s : game.getShips()) {
							if (s.pointBelongsToShip(lineP)) {
								events.add(new GameEvent(lineP, null, Effect.SHIP_COLLISION, null));
							}
						}
						break;
					}
					last = lineP;
				}
			}

			if (this.canKamikaze) {
				if (dy == 2) {
					this.facing = Direction.DOWN;
				} else if (dy == -2) {
					this.facing = Direction.UP;
				} else if (dx == 2) {
					this.facing = Direction.RIGHT;
				} else if (dx == -2) {
					this.facing = Direction.LEFT;
				}
			}
		}

		// Once we've moved, explode any adjacent mines
		explodeSurroundingMines(events);

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

	public void setLongRangeRadarEnabled(boolean longRangeRadarEnabled) {
		this.longRangeRadarEnabled = longRangeRadarEnabled;
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
	 * @param events The list of events to append to
	 */
	public void turnShip(Direction d, List<GameEvent> events) {
		List<Point> radiusPoints = getPointsInTurnRadius(d);

		// Sort the points based on the distance to the head
		final Point2D head = new Point2D.Double(position.x, position.y);
		Collections.sort(radiusPoints, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) Math.round(o1.distanceSq(head) - o2.distanceSq(head));
			}
		});

		ObstacleType obstacleType = game.getClosestObstacleType(radiusPoints);
		if (obstacleType == null) {
			Point pivot = getShipLine().getTail();
			setPosition(new Line(pivot, d, this.size).getHead());
			this.facing = d;
		} else {
			Point obstaclePoint = game.getClosestObstaclePosition(radiusPoints);
			if (obstacleType == ObstacleType.MINE && !isMineLayer()) {
				// Explode the mine
				game.explodeMine(obstaclePoint, events);
				// Damage the block that hit it
				doMineDamage(getCollidingBlockPosition(obstaclePoint, d));

			} else {
				events.add(new GameEvent(obstaclePoint, null, Effect.SHIP_COLLISION, null));
			}
		}
	}


	/**
	 * @param d
	 *            the direction the ship would face
	 * @return the list points the ship would traverse if it turned
	 */
	public List<Point> getPointsInTurnRadius(Direction d) {
		List<Point> points = new ArrayList<Point>();
		Point pivot = getShipLine().getTail();
		Line newLine = new Line(pivot, d, this.size + 1);//includes point in front of potential head
		Line temp;
		Line shipLine = null;
		if(this.facing == Direction.DOWN) {
			shipLine = new Line(pivot, new Point(position.x, position.y+1));
		} else if(this.facing == Direction.UP) {
			shipLine = new Line(pivot, new Point(position.x, position.y-1));
		} else if(this.facing == Direction.RIGHT) {
			shipLine = new Line(pivot, new Point(position.x+1, position.y));
		} else if(this.facing == Direction.LEFT) {
			shipLine = new Line(pivot, new Point(position.x-1, position.y));
		}
		for(int i = this.size; i>=1; i--)
		{
			temp = new Line(shipLine.getPoints().get(i), newLine.getPoints().get(i));
			// For the last line, remove first and last points

			if (i == this.size) {
				List<Point> tempPoints = temp.getPoints();
				tempPoints.remove(0);
				tempPoints.remove(tempPoints.size() - 1);
				points.addAll(tempPoints);
			} else {
				points.addAll(temp.getPoints());
			}
			points.remove(shipLine.getPoints().get(i));
		}
		return points;
	}

	/**
	 * @param p
	 *            the position of the colliding object
	 * @param turnDirection The direction we are turning to face
	 * @return the position of the square on the ship colliding with p
	 */
	public Point getCollidingBlockPosition(Point p, Direction turnDirection) {
		// Go through each line, testing if the point is in there
		List<Point> points = new ArrayList<Point>();
		Point pivot = getShipLine().getTail();
		Line newLine = new Line(pivot, turnDirection, this.size + 1);
		Line temp;
		Line shipLine = null;
		if(this.facing == Direction.DOWN) {
			shipLine = new Line(pivot, new Point(position.x, position.y+1));
		} else if(this.facing == Direction.UP) {
			shipLine = new Line(pivot, new Point(position.x, position.y-1));
		} else if(this.facing == Direction.RIGHT) {
			shipLine = new Line(pivot, new Point(position.x+1, position.y));
		} else if(this.facing == Direction.LEFT) {
			shipLine = new Line(pivot, new Point(position.x-1, position.y));
		}
		for(int i = this.size; i>=1; i--)
		{
			temp = new Line(shipLine.getPoints().get(i), newLine.getPoints().get(i));
			if (temp.getPoints().contains(p)) {
				return getShipLine().getPoints().get(i - 1);
			}
		}
		throw new IllegalArgumentException(p + " is not in one of the turn lines.");
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

		//special case for Kamikaze boat, where movement range == radar range
		if(this.canKamikaze())
			return this.getRadarRange().getPoints(this);

		if (this.facing == Direction.UP) {
			// The point below (behind) the ship
			int backY = this.position.y + this.size;
			Point backPoint = new Point(this.position.x, backY);
			points.add(backPoint);

			// Add all the points to the right of the ship
			Point sidePoint = new Point(this.position.x + 1, this.position.y + (getSize() / 2));
			points.add(sidePoint);

			// Add all the points to the left of the ship
			sidePoint = new Point(this.position.x - 1, this.position.y + (getSize() / 2));
			points.add(sidePoint);

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
			Point sidePoint = new Point(this.position.x + 1, this.position.y - (getSize() / 2));
			points.add(sidePoint);

			// Add all the points to the left of the ship
			sidePoint = new Point(this.position.x - 1, this.position.y - (getSize() / 2));
			points.add(sidePoint);

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
			Point sidePoint = new Point(this.position.x + (getSize() / 2), this.position.y - 1);
			points.add(sidePoint);

			// Add all the points right below the ship
			sidePoint = new Point(this.position.x + (getSize() / 2), this.position.y + 1);
			points.add(sidePoint);

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
			Point sidePoint = new Point(this.position.x - getSize() / 2, this.position.y - 1);
			points.add(sidePoint);

			// Add all the points below the ship
			sidePoint = new Point(this.position.x - getSize() / 2, this.position.y + 1);
			points.add(sidePoint);

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
	 * @return a map containing the points to turn to as well as the new facing direction.
	 */
	private Map<Point, Direction> getTurnPoints() {
		Map<Point, Direction> points = new HashMap<>();

		int yDelta = (facing == Direction.LEFT || facing == Direction.RIGHT) ? 1 : 0;
		int xDelta = (facing == Direction.LEFT || facing == Direction.RIGHT) ? 0 : 1;

		points.put(new Point(position.x + xDelta, position.y + yDelta),
				(facing == Direction.LEFT || facing == Direction.DOWN) ? facing.getCounterClockwise() :
				facing.getClockwise());
		points.put(new Point(position.x - xDelta, position.y - yDelta),
				(facing == Direction.RIGHT || facing == Direction.UP) ? facing.getCounterClockwise() : facing.getClockwise());

		if (canTurn180()) {
			// Get the last point
			Point2D tail = getShipLine().getP1();
			points.put(new Point((int)tail.getX() + (yDelta * (facing == Direction.RIGHT ? - 1 : 1)),
					(int)tail.getY() + (xDelta * (facing == Direction.DOWN ? - 1 : 1))), facing.opposite());
		}

		return points;
	}

	/**
	 * @return the set of points we can turn to.
	 */
	public Set<Point> getValidTurnPoints() {
		return game.getField().filterInBoundPoints(getTurnPoints().keySet());
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
	public Set<Point> getSurroundingPoints() {
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

		return game.getField().filterInBoundPoints(points);
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
					if (game.getField().getCellType(adjP) != CellType.WATER &&
							game.getField().getCellType(adjP) != CellType.MINE) {
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

	/**
	 * @return true if the ship can move, or false if its LRR is enabled
	 */
	public boolean canMove() {
		return !(hasLongRangeRadar && isLongRangeRadarEnabled());
	}


	/**
	 * Turns a ship to a point.
	 * @param p
	 */
	public void turnTo(Point p, List<GameEvent> events) {
		if (turnsOnCenter) {
			// Get the direction of the point we're turning to
			Direction newFacingDirection = getTurnPoints().get(p);

			boolean left = (facing.opposite() == newFacingDirection) || (facing.getCounterClockwise() == newFacingDirection);
			int turns = 1;

			// If it is opposite, we're turning 180
			if (facing.opposite() == newFacingDirection) {
				turns = 2;
			}


			for (int i = 0; i < turns; i++) {
				// If we encountered a collision, back up.
				if (!turnOnCenter(left, events)) {
					break;
				}

				// Once we've moved, explode any adjacent mines
				if (explodeSurroundingMines(events)) {
					// Break to ensure we only turn 90degrees if we encounter mines along the first turn.
					break;
				}
			}

		} else {
			turnShip(getTurnPoints().get(p), events);
			explodeSurroundingMines(events);
		}
	}

	/**
	 * Explodes all mine surrounding the ship.
	 * @param events The list of events to append to.
	 * @return true if at least one mine was hit
	 */
	private boolean explodeSurroundingMines(List<GameEvent> events) {
		// Once we've moved, explode any adjacent mines
		boolean hitMine = false;
		if (!isMineLayer()) {
			for (Point minePoint : getSurroundingPoints()) {
				if (game.getField().getCellType(minePoint) == CellType.MINE) {
					game.explodeMine(minePoint, events);
					hitMine = true;
				}
			}
		}
		return hitMine;
	}

	/**
	 * Turns a 3-size ship on its center.
	 * @param left True if we're turning left
	 * @param event
	 * @return true if the turn was succesfull
	 */
	public boolean turnOnCenter(boolean left, List<GameEvent> events) {
		if (this.getSize() != 3) {
			throw new RuntimeException("Hard-coded for size 3 ships yo.");
		}

		List<Point> points = new ArrayList<Point>();
		Point finalPoint = null;
		if (facing == Direction.LEFT) {
			points.add(new Point(position.x, position.y + (left ? 1 : -1)));
			points.add(new Point(position.x + 2, position.y + (left ? -1 : 1)));
			points.add(new Point(position.x + 1, position.y - 1));
			points.add(new Point(position.x + 1, position.y + 1));
			finalPoint = new Point(position.x + 1, position.y + (left ? 1 : -1));
		} else if (facing == Direction.RIGHT) {
			points.add(new Point(position.x, position.y + (left ? -1 : +1)));
			points.add(new Point(position.x - 2, position.y + (left ? 1 : -1)));
			points.add(new Point(position.x - 1, position.y - 1));
			points.add(new Point(position.x - 1, position.y + 1));
			finalPoint = new Point(position.x - 1, position.y + (left ? -1 : 1));
		} else if (facing == Direction.DOWN) {
			points.add(new Point(position.x + (left? 1 : - 1), position.y));
			points.add(new Point(position.x + (left? -1 : 1), position.y - 2));
			points.add(new Point(position.x - 1, position.y - 1));
			points.add(new Point(position.x + 1, position.y - 1));
			finalPoint = new Point(position.x + (left? 1 : -1), position.y - 1);
		} else if (facing == Direction.UP) {
			points.add(new Point(position.x + (left? -1 : 1), position.y));
			points.add(new Point(position.x + (left? 1 : -1), position.y + 2));
			points.add(new Point(position.x - 1, position.y + 1));
			points.add(new Point(position.x + 1, position.y + 1));
			finalPoint = new Point(position.x + (left? -1 : 1), position.y + 1);
		}

		// Test all points
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			// Test for ship collisions
			for(Ship ship : getGame().getShips()){
				if(ship.pointBelongsToShip(p)){
					// Ship collision!
					events.add(new GameEvent(p, null, Effect.SHIP_COLLISION, null));
					return false;
				}
			}
			// Test for non water collision
			if (game.getField().getCellType(p) != CellType.WATER) {
				// Collision!
				events.add(new GameEvent(p, null, Effect.SHIP_COLLISION, null));
				return false;
			}
		}

		setPosition(finalPoint);
		setDirection(left ? facing.getCounterClockwise() : facing.getClockwise());
		return true;

	}

	public void explode(List<GameEvent> events) {
		// Add the event at the ships point
		events.add(new GameEvent(getPosition(), Cause.KAMIKAZE, Effect.SHIP_EXPLODED, this));

		Point[] offsets = new Point[]{
				new Point(position.x - 1, position.y - 1),
				new Point(position.x - 1, position.y),
				new Point(position.x - 1, position.y + 1),
				new Point(position.x, position.y - 1),
				new Point(position.x, position.y + 1),
				new Point(position.x + 1, position.y - 1),
				new Point(position.x + 1, position.y),
				new Point(position.x + 1, position.y + 1),
		};

		// Damage all surrounding squares
		List<Point> damagedPoints = new ArrayList<>();
		List<Ship> damagedShips = new ArrayList<Ship>();
		for (Point p : offsets) {
			// Test for ships
			for (Ship s : game.getShips()) {
				if (s.pointBelongsToShip(p)) {
					int healthIndex = s.getShipLine().getPoints().indexOf(p);

					s.health[healthIndex] = 0;
					damagedPoints.add(p);
					damagedShips.add(s);
					break;
				}
			}
			// Test for base
			if (game.getBasePoints(game.getP1()).contains(p) ||
					game.getBasePoints(game.getP2()).contains(p)) {
				game.getField().damageBase(p);
				events.add(new GameEvent(p, Cause.KAMIKAZE, Effect.BASE_DESTROYED, null));
			}
		}

		// Need to show game events after in case we hit a ship twice, so that one effect
		// doesn't show hit and the other sunk.
		for (int i = 0; i < damagedPoints.size(); i++) {
			events.add(new GameEvent(damagedPoints.get(i), Cause.KAMIKAZE,
					damagedShips.get(i).isSunk() ? Effect.SHIP_SUNK : Effect.SHIP_HIT,
					damagedShips.get(i)));
		}


		// Destroy the kamikaze ship after
		for (int i = 0; i < health.length; i++) {
			health[i] = 0;
		}
	}
}
