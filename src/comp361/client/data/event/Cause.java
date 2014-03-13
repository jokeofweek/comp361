package comp361.client.data.event;

public enum Cause {
	CANNON("Cannon"),
	TORPEDO("Torpedo"),
	MINE("Mine");
	
	private String text;
	private Cause(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
}
