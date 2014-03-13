package comp361.shared.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comp361.shared.Constants;

/**
 * This class represents a game field.
 */
public class Field {

	private CellType[][] cells;
	private int[][] baseHealth;
	
	public Field() {
	}
	
	public Field(int width, int height) {
		// Instantiate every cell to water with no bomb
		cells = new CellType[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = CellType.WATER;
			}
		}
		
		// Set up the base health
		baseHealth = new int[2][Constants.BASE_HEIGHT];
		// Initialize health to 1
		for (int p = 0; p < baseHealth.length; p++) {
			for (int i = 0; i < baseHealth[p].length; i++) {
				baseHealth[p][i] = 1;
			}
		}
	}
	
	public CellType getCellType(Point p) {
		return cells[(int)p.getX()][(int)p.getY()];
	}
	
	public void setCellType(Point p, CellType cellType) {
		cells[(int)p.getX()][(int)p.getY()] = cellType;
	}
	
	public CellType[][] getCellTypeArray()
	{
		return cells;
	}
	
	/**
	 * Tests whether a point is in bounds.
	 * @param p The point itself.
	 * @return True if the point is in bounds.
	 */
	public boolean inBounds(Point p) {
		return (p.x >= 0 && p.x < Constants.MAP_WIDTH && p.y >= 0 && p.y < Constants.MAP_HEIGHT);
	}

	/**
	 * This filters a set of points to keep only in bound points.
	 * @param points The set of points to filter.
	 * @return The filtered set.
	 */
	public Set<Point> filterInBoundPoints(Set<Point> points) {
		Set<Point> inPoints = new HashSet<>(points.size());
		for (Point p : points) {
			if (inBounds(p)) {
				inPoints.add(p);
			}
		}
		return inPoints;
	}
	
	/**
	 * This filters a list of points to keep only in bound points.
	 * @param points The set of points to filter.
	 * @return The filtered set.
	 */
	public List<Point> filterInBoundPoints(List<Point> points) {
		List<Point> inPoints = new ArrayList<>(points.size());
		for (Point p : points) {
			if (inBounds(p)) {
				inPoints.add(p);
			}
		}
		return inPoints;
	}
	
	/**
	 * @param p A point on the field
	 * @return a set containing all adjacent points to p
	 */
	public Set<Point> getAdjacentPoints(Point p)
	{
		HashSet<Point> points = new HashSet<Point>();
		if (p.y != Constants.MAP_HEIGHT - 1) {
			points.add(new Point((int)p.getX(), (int)p.getY()+1));
		}
		if (p.y != 0) {
			points.add(new Point((int)p.getX(), (int)p.getY()-1));
		}
		if (p.x != 0) {
			points.add(new Point((int)p.getX()-1, (int)p.getY()));
		}
		if (p.x != Constants.MAP_WIDTH - 1) {
			points.add(new Point((int)p.getX()+1, (int)p.getY()));
		}
		return points;
	}
	
	/**
	 * @param p A point on the field
	 * @return a set containing all points where mines exist
	 */
	public Set<Point> getAdjacentMines(Point p)
	{
		HashSet<Point> points = new HashSet<Point>();
		for(Point point : getAdjacentPoints(p))
			if(getCellType(point) == CellType.MINE)
				points.add(point);
		return points;
	}
	
	/**
	 * Gets the first index for the base health array based on a given
	 * point.
	 * @param p The point to get the player index for.
	 * @return The player index.
	 */
	private int getBasePlayerIndex(Point p) {
		if (p.x == 0) {
			return 0; 
		} else if (p.x == Constants.MAP_WIDTH - 1) {
			return 1;
		} else {
			throw new IllegalArgumentException("No player has a base at this point!");
		}
	}
	
	/**
	 * Gets the second index for the base health array based on a given
	 * point.
	 * @param p The point to get the base position for.
	 * @return The base position.
	 */
	private int getBasePositionIndex(Point p) {
		return p.y - Constants.BASE_Y_OFFSET;
	}
	
	/**
	 * @param p Point on the base to inflict damage to
	 */
	public void damageBase(Point p)
	{	
		baseHealth[getBasePlayerIndex(p)][getBasePositionIndex(p)] = 0;
	}
	
	/**
	 * Tests whether a given base cell is your own base.
	 * @param p The base point.
	 * @param isP1 If you are player 1
	 * @return True if that base is your own.
	 */
	public boolean isOwnBase(Point p, boolean isP1) {
		return getBasePlayerIndex(p) == (isP1 ? 0 : 1);
	}
	
	/**
	 * Tests whether a given point is a base and is destroyed
	 * @param p The point to test
	 * @return True if the base is destroyed, else false.
	 */
	public boolean isBaseDestroyed(Point p) {
		return baseHealth[getBasePlayerIndex(p)][getBasePositionIndex(p)] == 0;
	}
	
	/**
	 * This gets the position where a ship should start based on an index.
	 * @param s	 The ship in question.
	 * @param index The index to place the ship at.
	 * @return The point where the ship should be positioned.
	 */
	public Point getIndexPosition(Ship s, int index)
	{
		int x = s.getOwner().equals(s.getGame().getP1()) ? 0
				: Constants.MAP_WIDTH - 1;
		int xOffset = s.getOwner().equals(s.getGame().getP1()) ? 1 : -1;


		// If the ship was at index 0 or passed the length of the base, we just
		// put it at x.
		if (index == 0) {
			return new Point(x - xOffset + (xOffset * s.getSize()),
					Constants.BASE_Y_OFFSET - 1);
		} else if (index == Constants.BASE_HEIGHT + 1) {
			return new Point(x - xOffset + (xOffset * s.getSize()),
					Constants.BASE_Y_OFFSET + Constants.BASE_HEIGHT);
			
		}

		// Position it at the right place on the ship
		index--;
		return new Point(x + (xOffset * s.getSize()), Constants.BASE_Y_OFFSET + index);
	}
}
