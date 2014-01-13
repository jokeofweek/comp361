package comp361.server.data;

import java.util.HashMap;
import java.util.Map;

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
	
	public boolean isAccountConnected(String accountName) {
		return this.accounts.containsKey(accountName);
	}
	
	public void removeAccount(String accountName) {
		this.accounts.remove(accountName);
	}
}
