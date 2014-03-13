package comp361.client.data;

import java.util.Observable;

import comp361.client.data.event.GameEvent;


public class EventTooltipContext extends Observable{

	private GameEvent event;
	
	public GameEvent getEvent() {
		return event;
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
