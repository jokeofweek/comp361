package comp361.client.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;


/**
 * This class is used by the client to keep track of all connected
 * player's information.
 */
public class PlayerManager extends Observable {

	private Map<String, Player> players;
	
	public PlayerManager() {
		this.players = new HashMap<>();
	}
	
	public void addPlayer(Player player) {
		this.players.put(player.getName(), player);
		setChanged();
		notifyObservers();
	}
	
	public Map<String, Player> getMap()
	{
		return players;
	}
	
	public Set<String> getPlayers() {
		return players.keySet();
	}	
	
	public void removePlayer(String name) {
		this.players.remove(name);
		setChanged();
		notifyObservers();
	}
	
}
