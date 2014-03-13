package comp361.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import comp361.shared.data.Player;
import comp361.shared.packets.server.PlayerListPacket;

/**
 * This class keeps track of all currently connected {@link Account} objects.
 */
public class AccountManager {

	private Map<String, Account> accounts;
	
	public AccountManager() {
		this.accounts = new HashMap<String, Account>();
	}
	
	public void addAccount(Account account) {
		this.accounts.put(account.getName(), account);
	}
	
	public Set<String> getAccounts() {
		return accounts.keySet();
	}
	
	public Account getAccount(String name) {
		return accounts.get(name);
	}
	
	public boolean isAccountConnected(String accountName) {
		return this.accounts.containsKey(accountName);
	}
	
	public void removeAccount(String accountName) {
		this.accounts.remove(accountName);
	}
	
	public PlayerListPacket getPlayerListPacket() {
		PlayerListPacket listPacket = new PlayerListPacket();

		// Iterate through all players, copying over the necessary data.
		listPacket.players = new ArrayList<Player>();
		for (Account account : accounts.values()) {
			listPacket.players.add(account.getPlayer());
		}
				
		return listPacket;
	}
}
