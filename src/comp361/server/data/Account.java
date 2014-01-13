package comp361.server.data;

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
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}	
	
}
