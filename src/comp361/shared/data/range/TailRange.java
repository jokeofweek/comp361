package comp361.shared.data.range;

import java.awt.Point;
import java.awt.Rectangle;

import comp361.shared.data.Ship;

public class TailRange extends Range {

	public TailRange(int width, int height) {
		super(width, height);
	}

	@Override
	public Rectangle getVisibleRectangle(Ship source) {
		// Get the point before the tail.
		Point p = source.getShipLine().getPoints().get(1);

		switch (source.getDirection()) {
		case RIGHT:
			return new Rectangle(p.x, p.y - ((getHeight() - 1) / 2),
					getWidth(), getHeight());

		}
		// TODO Auto-generated method stub
		return null;
	}

}
