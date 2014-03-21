package comp361.client.data.event;

public enum Effect {
	SHIP_COLLISION("Ship Collision"),
	SHIP_EXPLODED("Ship Exploded"),
	MINE_EXPLODED("Mine Exploded"),
	MINE_DESTROYED("Mine Destroyed"),
	SHIP_HIT("Ship Hit"), 
	BASE_HIT("Base Hit"), 
	SHIP_SUNK("# Sunk"), 
	BASE_DESTROYED("Base Destroyed"),
	HIT_WATER("Hit Nothing");
	
	private String text;
	private Effect(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
}
