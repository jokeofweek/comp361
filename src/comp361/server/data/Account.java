package comp361.server.data;

import comp361.shared.data.Player;

/**
 * An account used for playing Battleships.
 */
public class Account {

	private String name;
	private String password;
	private Player player;
	
	public Account(String name) {
		this.name = name;
		this.player = new Player(name);
	}
	
	public String getName() {
		return name;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return a {@link Player} object which can be sent to the client.
	 */
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}	
	
}
