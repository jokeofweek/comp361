package comp361.client.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import comp361.shared.data.Player;
import comp361.shared.data.Statistics;
import comp361.shared.packets.server.PlayerListPacket;


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
		internalAddPlayer(player);
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
	
	public Player getPlayer(String name) {
		return this.players.get(name);
	}
	
	public void removePlayer(String name) {
		this.players.remove(name);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Helper method for inserting a player without notifying observers.
	 * @param player The player to add.
	 */
	private void internalAddPlayer(Player player) {
		this.players.put(player.getName(), player);		
	}
	
	/**
	 * Updates the list of players according to a {@link PlayerListPacket}, 
	 * effectively adding all players at once.
	 * @param packet
	 */
	public void updatePlayerList(PlayerListPacket packet) {
		for (Player player : packet.players) {
			internalAddPlayer(player);
		}
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Updates a player's stats.
	 * @param player The player.
	 * @param stats The stats.
	 */
	public void updateStatistics(String player, Statistics stats) {
		Player p = this.players.get(player);
		if (p == null) return;
		p.setStatistics(stats);
		setChanged();
		notifyObservers();
	}
}
