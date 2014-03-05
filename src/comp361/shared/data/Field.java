package comp361.shared.data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a game field.
 */
public class Field {

	private CellType[][] cells;
	private int[][] baseHealth;
	
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
	
	/**
	 * @param p A point on the field
	 * @return a set containing all adjacent points to p
	 */
	public Set<Point> getAdjacentPoints(Point p)
	{
		HashSet<Point> points = new HashSet<Point>();
		//TODO: implement this
		return points;
	}
	
	/**
	 * @param p A point on the field
	 * @return a set containing all points where mines exist
	 */
	public Set<Point> getAdjacentMines(Point p)
	{
		HashSet<Point> points = new HashSet<Point>();
		//TODO: implement this
		return points;
	}
	
	/**
	 * @param p Point on the base to inflict damage to
	 */
	public void damageBase(Point p)
	{
		
	}
	
	/**
	 * @return
	 */
	public Point getRandomFreeReefPosition()
	{
		Point p = new Point();
		//TODO: implement this
		return p;
	}
	
	/**
	 * @param s
	 * @param index
	 * @param d
	 * @return
	 */
	public Point getIndexPosition(Ship s, int index, Direction d)
	{
		Point p = new Point();
		//TODO: implement this
		return p;
	}
}
