package comp361.shared.data;

public enum Direction {
	LEFT, UP, RIGHT, DOWN;
	
	/**
	 * @param d a direction
	 * @return returns the opposite direction of d
	 */
	public Direction opposite()
	{
		return values()[(this.ordinal()+2)%4];
	}
	
	/**
	 * @return true if this is left or right, else false.
	 */
	public boolean alongXAxis() 
	{
		return this == LEFT || this == RIGHT;
	}
}
