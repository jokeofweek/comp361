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
	 * @return true if this is left or right, else false.
	 */
	public boolean isHorizontal()
	{
		return this == LEFT || this == RIGHT;
	}
	
	public Direction getClockwise() {
		return values()[(this.ordinal() + 1) % 4];
	}
	
	public Direction getCounterClockwise() {
		if (this == LEFT) return DOWN;
		else return values()[(this.ordinal() - 1) % 4];
	}
	
	/**
	 * @param other another direction
	 * @return returns true if other and this are perpendicular
	 */
	public boolean isPerpendicularTo(Direction other)
	{
		return this.ordinal() == (other.ordinal()+1)%4 || this.ordinal() == (other.ordinal()-1)%4;
	}
}
