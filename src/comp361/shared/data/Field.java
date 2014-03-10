package comp361.shared.data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import comp361.shared.Constants;

/**
 * This class represents a game field.
 */
public class Field {

	private CellType[][] cells;
	private int[][] baseHealth;
	
	public Field() {
		// TODO Auto-generated constructor stub
	}
	
	public Field(int width, int height) {
		// Instantiate every cell to water with no bomb
		cells = new CellType[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = CellType.WATER;
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
	 * @param p Point on the base to inflict damage to
	 */
	public void damageBase(Point p)
	{
		//TODO: implement this
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
