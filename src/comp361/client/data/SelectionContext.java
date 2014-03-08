package comp361.client.data;

import java.util.Observable;

import comp361.shared.data.MoveType;
import comp361.shared.data.Ship;

public class SelectionContext extends Observable {

	private Ship ship;
	private MoveType type;

	public Ship getShip() {
		return ship;
	}

	public MoveType getType() {
		return type;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
		this.type = MoveType.MOVE;
		setChanged();
		notifyObservers();
	}

	public void setType(MoveType type) {
		this.type = type;
		setChanged();
		notifyObservers();
	}

}
