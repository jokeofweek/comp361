package comp361.client.data.event;

import java.awt.Point;

import comp361.shared.data.Ship;


public class GameEvent {

	private Point point;
	private Cause cause;
	private Effect effect;
	private Ship firingShip;
	
	public GameEvent(Point point, Cause cause, Effect effect, Ship firingShip) {
		this.point = point;
		this.cause = cause;
		this.effect = effect;
		this.firingShip = firingShip;
	}
	
	public Cause getCause() {
		return cause;
	}
	
	public Effect getEffect() {
		return effect;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public Ship getFiringShip() {
		return firingShip;
	}
	
}
