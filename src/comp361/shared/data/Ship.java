package comp361.shared.data;

import comp361.shared.data.radar.Radar;

public class Ship {

	// This is the position of the head of the ship.
	private int x;
	private int y;
	private int size;
	private int speed;
	private Direction facing;
	private ArmorType armor;
	private int[] health;
	private boolean turnsOnCenter;
	private boolean isMineLayer;
	private boolean hasLongRangeRadar;
	private boolean longRangeRadarEnabled;
	private Radar radar;
	private Radar longRadar;
	
}
