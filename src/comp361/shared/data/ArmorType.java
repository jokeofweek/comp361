package comp361.shared.data;

public enum ArmorType {
	NORMAL(1),
	HEAVY(2);
	
	int healthPointsPerSquare = 0;

	private ArmorType(int healthPoints) {
		this.healthPointsPerSquare = healthPoints;
	}
	
	public int getHealthPointsPerSquare() {
		return healthPointsPerSquare;
	}
}
