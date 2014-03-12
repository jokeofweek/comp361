package comp361.shared.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import comp361.client.ui.lobby.games.GamesPanel;

/**
 * A {@link GameDescriptor} represents a potential game. These could appear in the
 * {@link GamesPanel}.
 */
public class GameDescriptor {
	
	private int id;
	private int shipInventory;
	private String name;
	private String password;
	private String[] players;
	private int[][] positions;
	private Set<String> readyPlayers;
	private long seed;
	private int maxPlayers;
	private boolean started;
	
	/**
	 * This constructor should only be used by the Kryo serializer,
	 * as the id cannot be changed after construction.
	 */
	public GameDescriptor(){}
	
	public GameDescriptor(int id, String name, String password, int maxPlayers, int shipInventory) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.players = new String[maxPlayers];
		this.positions = new int[maxPlayers][Ship.SHIP_INVENTORIES[shipInventory].length];
		this.shipInventory = shipInventory;
		this.readyPlayers = new HashSet<>();
		this.maxPlayers = maxPlayers;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String[] getPlayers() {
		return players.clone();
	}
	
	public Set<String> getReadyPlayers() {
		return readyPlayers;
	}

	public int[][] getPositions() {
		return positions;
	}
	
	public void setPositions(int index, int[] position) {
		for (int i = 0; i < position.length; i++) {
			this.positions[index][i] = position[i];
		}
	}
	
	public void addPlayer(String name) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = name;
				return;
			}
		}
	}
	
	public void removePlayer(String name) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].equals(name)) {
				players[i] = null;
				break;
			}
		}
		removeReadyPlayer(name);
	}
	
	public void addReadyPlayer(String name) {
		readyPlayers.add(name);
	}

	public void removeReadyPlayer(String name) {
		readyPlayers.remove(name);
	}
		
	public void clearReadyPlayers() {
		readyPlayers.clear();
	}
		
	public int getMaxPlayers() {
		return maxPlayers;
	}

	public boolean isStarted() {
		return started;
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getShipInventory() {
		return shipInventory;
	}
	
	public int getPlayerCount() {
		int count = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				count++;
			}
		}
		return count;
	}

	public boolean isPrivate() {
		return !getPassword().isEmpty();
	}
}
