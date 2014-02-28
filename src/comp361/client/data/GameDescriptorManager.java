package comp361.client.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import comp361.shared.data.GameDescriptor;

public class GameDescriptorManager extends Observable {

	public Map<Integer, GameDescriptor> gameDescriptors;
	public int currentId;
	
	public GameDescriptorManager(){
		this.gameDescriptors = new HashMap<>();
	}
	
	public void addDescriptor(GameDescriptor descriptor) {
		this.gameDescriptors.put(descriptor.getId(), descriptor);
		setChanged();
		notifyObservers();
	}

	public Collection<GameDescriptor> getGameDescriptors() {
		return gameDescriptors.values();
	}
	
	public GameDescriptor getGameDescriptor(int id) {
		return gameDescriptors.get(id);
	}
	
	public Collection<Integer> getGameDescriptorIds() {
		return gameDescriptors.keySet();
	}
	
}
