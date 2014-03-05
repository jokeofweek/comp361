package comp361.shared.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comp361.shared.data.radar.Radar;

public class Ship{

	// This is the position of the head of the ship.
	private Point position;
	private Player owner;
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
	private Radar radar;
	private Radar longRadar;
	
	public Ship clone()
	{
		//TODO: implement this
		return null;
	}
	
	/**
	 * @return the size of the ship
	 */
	public int getSize()
	{
		return this.size;
	}
	
	/**
	 * @return the owner of the ship
	 */
	public Player getOwner()
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
	public Line2D getTorpedoLine()
	{
		Line2D line = new Line2D.Double(0, 0,0,0);
		//TODO: implement this
		return line;
	}
	
	/**
	 * Fires a torpedo
	 */
	public void fireTorpedo()
	{
		//if(this.hasTorpedoes)
			//TODO: implement this
	}
	
	/**
	 * @param p the target position of the enemy torpedo
	 * @param shootingDirection the direction of the incoming torpedo
	 */
	public void hitWithTorpedo(Point p, Direction shootingDirection)
	{
		//TODO: implement this
	}
	
	/**
	 * @return returns the line segment that the ship occupies
	 */
	public Line2D getLine()
	{
		Line2D line = new Line2D.Double(0,0,0,0);
		//TODO: implement this
		return line;
	}
	
	
	/**
	 * @param p the new position of the ship
	 */
	public void moveShip(Point p)
	{
		//TODO: implement this
	}
	
	/**
	 * @param p the target point
	 * @return the line segment between the ship and p
	 */
	public Line2D getLineTo(Point p)
	{
		Line2D line = new Line2D.Double(0,0,0,0);
		//TODO: implement this
		return line;
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
		//TODO: implement this
		return 0;
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
			//turn
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
		//TODO: implement this
		return false;
	}
	
	/**
	 * @param d the direction the ship would face
	 * @return the list points the ship would traverse if it turned
	 */
	public List<Point> getPointsInTurnRadius(Direction d)
	{
		List<Point> points = new ArrayList<Point>();
		//TODO: implement this
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
	public boolean pointBelongsToShip(Point p)
	{
		//TODO: implement this
		return false;
	}
}
