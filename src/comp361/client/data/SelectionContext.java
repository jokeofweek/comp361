package comp361.client.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import comp361.shared.data.MoveType;
import comp361.shared.data.Ship;

public class SelectionContext extends Observable {

	private Ship ship;
	private MoveType type;
	private Collection<Point> points;

	public Ship getShip() {
		return ship;
	}

	public MoveType getType() {
		return type;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
		this.type = MoveType.MOVE;
		updatePoints();
		setChanged();
		notifyObservers();
	}

	public void setType(MoveType type) {
		this.type = type;
		updatePoints();
		setChanged();
		notifyObservers();
	}

	public Collection<Point> getPoints() {
		return points;
	}
	
	public void updatePoints() {
		if (ship == null || type == null) {
			points = new ArrayList<>();
		} else if (getType() == MoveType.MOVE) {
			points = ship.getValidMovePoints();
		} else if (getType() == MoveType.CANNON) {
			points = ship.getCannonRange().getPoints(ship);
		} else if (getType() == MoveType.PICKUP_MINE) {
			points = ship.getValidMinePickupPoints();
		} else {
			points = new ArrayList<>();
		}
	}
}
