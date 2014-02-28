package comp361.shared.data;

import comp361.client.ui.lobby.games.GamesPanel;

/**
 * A {@link GameDescriptor} represents a potential game. These could appear in the
 * {@link GamesPanel}.
 */
public class GameDescriptor {
	
	private int id;
	private String name;
	private String password;
	private int currentPlayers;
	private int maxPlayers;
	private boolean started;
	
	public GameDescriptor(int id, String name, String password, int maxPlayers) {
		this.id = id;
		this.name = name;
		this.password = password;
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

	public int getCurrentPlayers() {
		return currentPlayers;
	}
	
	public void setCurrentPlayers(int currentPlayers) {
		this.currentPlayers = currentPlayers;
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
	
	
}
