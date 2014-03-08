package comp361.shared.data.radar;

/**
 * All different types of radar will extend this class.
 */
public abstract class Radar {

	private int width;
	private int height;

	public Radar(int width, int height) {
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
	 * @param destinationX The X position that we are trying to see.
	 * @param destinationY The Y position that we are trying to see.
	 * @return True if the destination is in the radar's range.
	 */
	public abstract boolean inRange(int destinationX, int destinationY);
	
}