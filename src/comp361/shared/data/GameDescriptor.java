package comp361.shared.data;

import java.util.HashSet;
import java.util.Set;

import comp361.client.ui.lobby.games.GamesPanel;

/**
 * A {@link GameDescriptor} represents a potential game. These could appear in the
 * {@link GamesPanel}.
 */
public class GameDescriptor {
	
	private int id;
	private String name;
	private String password;
	private Set<String> players;
	private Set<String> readyPlayers;
	private long seed;
	private int maxPlayers;
	private boolean started;
	
	/**
	 * This constructor should only be used by the Kryo serializer,
	 * as the id cannot be changed after construction.
	 */
	public GameDescriptor(){}
	
	public GameDescriptor(int id, String name, String password, int maxPlayers) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.players = new HashSet<>();
		this.readyPlayers = new HashSet<>();
		this.maxPlayers = maxPlayers;
		this.seed = seed;
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

	public Set<String> getPlayers() {
		return new HashSet<>(players);
	}
	
	public Set<String> getReadyPlayers() {
		return readyPlayers;
	}

	public void addPlayer(String name) {
		players.add(name);
	}
	
	public void removePlayer(String name) {
		players.remove(name);
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

	/**
	 * @return true if the game descriptor has players, else false.
	 */
	public boolean hasPlayers() {
		return players.size() > 0;
	}	
	
	
	
}
