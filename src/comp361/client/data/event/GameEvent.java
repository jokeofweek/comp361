package comp361.client.data.event;

import java.awt.Point;
import java.util.Collection;
import java.util.List;

import comp361.shared.data.Ship;


public class GameEvent {

	private Collection<Point> points;
	private Cause cause;
	private List<Effect> effects;
	private Ship firingShip;
	
	public GameEvent(Collection<Point> points, Cause cause, List<Effect> effects, Ship firingShip) {
		this.points = points;
		this.cause = cause;
		this.effects = effects;
		this.firingShip = firingShip;
	}
	
	public Cause getCause() {
		return cause;
	}
	
	public List<Effect> getEffects() {
		return effects;
	}
	
	public Collection<Point> getPoints() {
		return points;
	}
	
	public Ship getFiringShip() {
		return firingShip;
	}
	
}
