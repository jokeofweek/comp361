package comp361.shared.data.range;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import comp361.shared.data.Ship;

/**
 * All different types of radar will extend this class.
 */
public abstract class Range {

	protected int width;
	protected int height;
	
	public Range(){};

	public Range(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * This function differs on the type of radar, and will be used to test whether a given radar
	 * can see a position.
	 * @param source The source ship.
	 * @param destinationX The X position that we are trying to see.
	 * @param destinationY The Y position that we are trying to see.
	 * @return True if the destination is in the radar's range.
	 */
	public boolean inRange(Ship source, int destinationX, int destinationY) {
		return getRectangle(source).contains(destinationX, destinationY);
	}
	
	/**
	 * Build a {@link Rectangle} containing the visible area.
	 * @param source The source ship.
	 * @return A rectangle representing the visible area.
	 */
	public abstract Rectangle getRectangle(Ship source);
	
	/**
	 * Creates a set of all the points contained in the range for a given range.
	 * @param source The source ship.
	 * @return The set of all points in the rectangle returned by getRectnagle.
	 */
	public Set<Point> getPoints(Ship source) {
		Rectangle rect = getRectangle(source);
		Set<Point> points = new HashSet<>(rect.width * rect.height);
		for (int x = 0; x < rect.width; x++) {
			for (int y = 0; y < rect.height; y++) {
				points.add(new Point(rect.x + x, rect.y + y));
			}
		}
		return points;
	}
}