package comp361.server.data;

import comp361.shared.data.Player;

/**
 * An account used for playing Battleships.
 */
public class Account {

	private String name;
	private String password;
	
	public Account() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
		// TODO: This is where statistics would be loaded to be sent to the client.
		return new Player(name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}	
	
}
