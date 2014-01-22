package observation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marc
 * Interface for making a class observable
 * 
 */
public interface Observable 
{
	List<Observer> observers = new ArrayList<Observer>();
	
	void addObserver(Observer obs);
	
	void removeObserver(Observer obs);

	void notifyObservers();
}
