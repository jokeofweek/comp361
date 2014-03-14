package comp361.client.data.event;

import java.awt.Point;

import comp361.shared.data.Ship;

public class GameEvent {

	private Point point;
	private Cause cause;
	private Effect effect;
	private Ship victimShip;
	private boolean playedSound;

	public GameEvent(Point point, Cause cause, Effect effect, Ship victimShip) {
		this.point = point;
		this.cause = cause;
		this.effect = effect;
		this.victimShip = victimShip;
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

	public Ship getVictimShip() {
		return victimShip;
	}

	public boolean getPlayedSound() {
		return playedSound;
	}

	public void setPlayedSound(boolean playedSound) {
		this.playedSound = playedSound;
	}
}
