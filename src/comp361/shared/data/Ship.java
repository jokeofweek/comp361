package comp361.shared.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comp361.shared.data.range.Range;
import comp361.shared.data.range.TailRange;

public class Ship{

	public static final Ship DESTROYER_TEMPLATE = new Ship(4, 8, 0, ArmorType.NORMAL, false, false, false, false, false, true, false, false, null, null);
	private static final int TORPEDO_RANGE = 10;
	
	// This is the position of the head of the ship.
	private Point position;
	private String owner;
	private Game game;
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
	
	
	public Ship(int size, int speed, int mineCount, ArmorType armor,
			boolean turnsOnCenter, boolean isMineLayer,
			boolean hasLongRangeRadar, boolean longRangeRadarEnabled,
			boolean hasHeavyCannon, boolean hasTorpedoes, boolean hasSonar,
			boolean canTurn180, Range radar, Range longRadar) {
		super();
		this.size = size;
		this.speed = speed;
		this.mineCount = mineCount;
		this.armor = armor;
		// TODO: Setup health
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
	}

	
	public Ship clone(Game game, String owner)
	{
		Ship s = new Ship(size, speed, mineCount, armor, turnsOnCenter, isMineLayer, hasLongRangeRadar, longRangeRadarEnabled, hasHeavyCannon, hasTorpedoes, hasSonar, canTurn180, radar, longRadar);
		s.owner = owner;
		s.game = game;
		return s;
	}
	
	/**
	 * @return the size of the ship
	 */
	public int getSize()
	{
		return this.size;
	}
	
	/**
	 * @return the game in which the ship is
	 */
	public Game getGame()
	{
		return this.game;
	}
	
	/**
	 * @return the owner of the ship
	 */
	public String getOwner()
	{
		return this.owner;
	}
	
	/**
	 * @return the speed of the ship
	 */
	public int getSpeed()
	{
		return this.speed;
	}
	
	/**
	 * @return the direction the ship is facing
	 */
	public Direction getDirection()
	{
		return this.facing;
	}
	
	/**
	 * @param direction the new direction the ship should face
	 */
	public void setDirection(Direction direction)
	{
		this.facing = direction;
	}
	
	/**
	 * @return the numbers of mines the ship is carrying
	 */
	public int getMineCount()
	{
		return this.mineCount;
	}
	
	/**
	 * @param mineCount the new number of mines the ship carries
	 */
	public void setMineCount(int mineCount)
	{
		this.mineCount = mineCount;
	}
	
	/**
	 * @return true if the ship is a Mine Layer, false otherwise
	 */
	public boolean isMineLayer()
	{
		return this.isMineLayer;
	}
	
	/**
	 * @return true if the ship is equipped with a heavy cannon, false otherwise
	 */
	public boolean hasHeavyCannon()
	{
		return this.hasHeavyCannon();
	}
	
	/**
	 * @return true if the ship can perform 180 degrees turns, false otherwise
	 */
	public boolean canTurn180()
	{
		return this.canTurn180;
	}
	
	/**
	 * @return the set of points reachable by the ship's cannon
	 */
	public Set<Point> getCannonRange()
	{
		Set<Point> points = new HashSet<Point>();
		//TODO: implement this
		return points;
	}
	
	/**
	 * @param p the target location of the cannon ball
	 * @return true if the operation succeeds, false otherwise
	 */
	public boolean fireCannon(Point p)
	{
		if(this.getCannonRange().contains(p))
		{
			//TODO: implement fire cannon functionality
			return true;
		}
		return false;
	}
	
	/**
	 * @param p the target point of the enemy cannon ball
	 * @param isHeavyCannon true if the enemy cannon ball is HEAVY, false otherwise
	 */
	public void hitWithCannon(Point p, boolean isHeavyCannon)
	{
		//TODO: implement this
	}
	
	/**
	 * @return true if the ship is equipped with torpedoes, false otherwise
	 */
	public boolean hasTorpedoes()
	{
		return this.hasTorpedoes;
	}
	
	/**
	 * @return the trajectory of the ship's torpedoes
	 */
	public Line getTorpedoLine()
	{
		Point firstPoint;
		if(facing == Direction.LEFT)
			firstPoint = new Point(position.x-1, position.y);
		if(facing == Direction.UP)
			firstPoint = new Point(position.x, position.y-1);
		if(facing == Direction.RIGHT)
			firstPoint = new Point(position.x+1, position.y);
		else
			firstPoint = new Point(position.x, position.y+1);
		return new Line(firstPoint, facing, TORPEDO_RANGE);
	}
	
	/**
	 * Fires a torpedo
	 */
	public void fireTorpedo()
	{
		if(this.hasTorpedoes)
		{
			for(Point p : getTorpedoLine().getPoints())
			{
				if(game.getField().getCellType(p) == CellType.BASE)
					game.getField().damageBase(p);
				for(Ship s : game.getShips())
					if(s.pointBelongsToShip(p))
						s.hitWithTorpedo(p, this.facing);
			}		
		}
	}
	
	/**
	 * @param p the target position of the enemy torpedo
	 * @param shootingDirection the direction of the incoming torpedo
	 */
	public void hitWithTorpedo(Point p, Direction shootingDirection)
	{
		//damage the right square
		if(health[getShipLine().getPoints().indexOf(p)] > 0)
			health[getShipLine().getPoints().indexOf(p)]--;
		//if perpendicular, damage another adjacent square
		if(Math.abs(shootingDirection.angleBetween(facing)) == Math.PI/2)
		{
			
		}
	}
	
	/**
	 * @return returns the line segment that the ship occupies
	 */
	public Line getShipLine()
	{
		Point tail = new Point(position);
		if(this.facing == Direction.UP)
			tail = new Point((int)position.getX(), (int)(position.getY()+size));
		if(this.facing == Direction.DOWN)
			tail = new Point((int)position.getX(), (int)(position.getY()-size));
		if(this.facing == Direction.LEFT)
			tail = new Point((int)position.getX()+size, (int)position.getY());
		if(this.facing == Direction.RIGHT)
			tail = new Point((int)position.getX()-size, (int)position.getY());
		return new Line(tail, position);
	}
	
	
	/**
	 * Moves the ship to the position, given there are no obstacles on the path.
	 * @param p the new position of the ship
	 */
	public void moveShip(Point p)
	{
		Line trajectory = getLineTo(p);
		if(!game.hasObstacle(trajectory.getPoints()))
			this.position = p;
	}
	
	/**
	 * This sets the position of the ship's head.
	 * @param position The new position.
	 */
	public void setPosition(Point position) {
		this.position = position;
	}
	
	/**
	 * @param p the target point
	 * @return the line segment between the ship and p
	 */
	public Line getLineTo(Point p)
	{
		return new Line(this.position, p);
	}
	
	/**
	 * @return true if the ship has a long range radar, false otherwise
	 */
	public boolean hasLongRangeRadar()
	{
		return this.hasLongRangeRadar;
	}
	
	/**
	 * @return true if the toggle operation is successful, false otherwise
	 */
	public boolean toggleLongRangeRadar()
	{
		try
		{
			this.longRangeRadarEnabled = !longRangeRadarEnabled;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * @return true if the long-range radar is enabled, false otherwise
	 */
	public boolean isLongRangeRadarEnabled()
	{
		return this.longRangeRadarEnabled;
	}
	
	/**
	 * @return true if the ship has a sonar, false otherwise
	 */
	public boolean hasSonar()
	{
		return this.hasSonar;
	}
	
	/**
	 * @param index index of a square in the ship
	 * @return the health of the square at index
	 */
	public int getHealth(int index)
	{
		return health[index];
	}
	
	/**
	 * @param index the index of the position on the base
	 */
	public void positionShip(int index)
	{
		//TODO: implement this
	}
	
	/**
	 * @param d the new direction of the ship
	 * @return true if the turning operation succeeds, false otherwise
	 */
	public boolean turnShip(Direction d)
	{
		if(this.canTurnToFace(d))
		{
			Point pivot;
			this.facing = d;
			if(this.turnsOnCenter)
			{
				//ship turns on its center
				pivot = getShipLine().getPoints().get(this.size/2);
				this.position = new Line(pivot, d, this.size/2).getHead();
			}
			else
			{
				//ship turns on its tail
				pivot = getShipLine().getTail();
				this.position = new Line(pivot, d, this.size).getHead();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @param d the direction the ship would face
	 * @return true if the ship can turn to the direction, false otherwise
	 */
	public boolean canTurnToFace(Direction d)
	{
		if(d == this.facing.opposite() && !canTurn180)
			return false;
		return !(game.hasObstacle(getPointsInTurnRadius(d))) ;
	}
	
	/**
	 * @param d the direction the ship would face
	 * @return the list points the ship would traverse if it turned
	 */
	public List<Point> getPointsInTurnRadius(Direction d)
	{
		List<Point> points = new ArrayList<Point>();
		Point pivot, corner1, corner2;
		if(turnsOnCenter)
		{
			pivot = getShipLine().getPoints().get(this.size/2);
			//if(d == facing.opposite())
				
		}
		else 
		{
			pivot = getShipLine().getTail();
		}
		return points;
	}
	
	/**
	 * @param p the position of the colliding object
	 * @return the position of the square on the ship colliding with p
	 */
	public Point getCollidingBlockPosition(Point p)
	{
		Point point = new Point();
		//TODO: implement this
		return point;
	}
	
	/**
	 * @param p the position where the mine will be dropped
	 * @return true if the mine drop operation succeeds, false otherwise
	 */
	public boolean dropMine(Point p)
	{
		if(this.isMineLayer() /*do more validation on position p*/)
		{
			//drop mine
			return true;
		}
		return false;
	}
	
	/**
	 * @param p A point on the field
	 * @return true if the point belongs to the ship, false otherwise
	 */
	public boolean pointBelongsToShip(Point p) {
		return getShipLine().contains(p);
	}
	
	/**
	 * @return the active radar, whether it's the long range or the regular range.
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
		return new HashSet<>();
	}
}
