package comp361.shared.data;

public enum Direction {
	LEFT, UP, RIGHT, DOWN;
	
	private double angle;
	
	/**
	 * @param d a direction
	 * @return the opposite direction of d
	 */
	public Direction opposite()
	{
		return values()[(this.ordinal()+2)%4];
	}
	
	/**
	 * @return the angle of the direction in radian
	 */
	public double angle()
	{
		return Math.PI-this.ordinal()*(Math.PI/2);		
	}
	
	/**
	 * @param d a direction
	 * @return the angle between this and d
	 */
	public double angleBetween(Direction d)
	{
		return d.angle() - this.angle;
	}
}
