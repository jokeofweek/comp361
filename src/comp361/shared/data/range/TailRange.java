package comp361.shared.data.range;

import java.awt.Point;
import java.awt.Rectangle;

import comp361.shared.data.Ship;

/**
 * This range starts from the tail.
 */
public class TailRange extends Range {

	public TailRange() {
		// TODO Auto-generated constructor stub
	}
	
	public TailRange(int width, int height) {
		super(width, height);
	}

	@Override
	public Rectangle getRectangle(Ship source) {
		// Get the point before the tail.
		Point p = source.getShipLine().getPoints().get(0);

		switch (source.getDirection()) {
		case LEFT:
			return new Rectangle(p.x - getWidth() + 1, p.y
					- ((getHeight() - 1) / 2), getWidth(), getHeight());
		case RIGHT:
			return new Rectangle(p.x, p.y - ((getHeight() - 1) / 2),
					getWidth(), getHeight());
		case DOWN:
			return new Rectangle(p.x - ((getHeight() - 1) / 2), p.y,
					getHeight(), getWidth());
		case UP:
			return new Rectangle(p.x - ((getHeight() - 1) / 2), p.y
					- getWidth() + 1, getHeight(), getWidth());
		}
		throw new RuntimeException();
	}

}
