package comp361.client.data;

import java.util.Observable;

import comp361.client.data.event.GameEvent;


public class EventTooltipContext extends Observable{

	private boolean isP1;
	private GameEvent event;
	
	public EventTooltipContext(boolean isP1) {
		this.isP1 = isP1;
	}
	
	public GameEvent getEvent() {
		return event;
	}
	
	public boolean isP1() {
		return isP1;
	}
	
	public void setEvent(GameEvent event) {
		if (event != this.event) {
			System.out.println("Updating " + event);
			this.event = event;
			setChanged();
			notifyObservers(event);
		}
	}
	
}
