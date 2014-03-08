package comp361.shared.data.range;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import comp361.shared.data.Ship;

public class CenterRange extends Range {

	public CenterRange() {
		// TODO Auto-generated constructor stub
	}
	
	public CenterRange(int width, int height) {
		super(width, height);
	}

	@Override
	public Rectangle getRectangle(Ship source) {
		int x = 0;
		int y = 0;
		//just helper values to take care of both orientations in a direction
		int baseX = 0;
		int baseY = 0;
		//if ship is facing up or down
		if(Math.abs(source.getDirection().angle()) == Math.PI/2)
		{
			//flip the width and height if the boat is vertical
			//since they are defined for when the boat is horizontal by the specs
			baseY = Math.min(source.getPosition().y, source.getShipLine().getTail().y);
			x = source.getPosition().x - (this.height-1)/2;
			y = baseY-(this.width-source.getSize())/2;
			return new Rectangle(new Point(x,y), new Dimension(this.height, this.width));
		}
		else
		{
			baseX = Math.min(source.getPosition().x, source.getShipLine().getTail().x);
			x = baseX-(this.width-source.getSize())/2;
			y = source.getPosition().y-(this.height-1)/2;
			return new Rectangle(new Point(x,y), new Dimension(this.width, this.height));
		}
	}

}
