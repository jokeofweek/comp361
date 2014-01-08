package comp361.server.data.store;

import comp361.server.data.Account;

public abstract class AccountDataStore {

	/**
	 * This tests whether an account exists for a given name.
	 * 
	 * @param accountName The name of the account
	 * @return	true if there is an account registered with that name.
	 */
	public abstract boolean accountExists(String accountName);
	
	/**
	 * Attempts to load the account for a given name. 
	 * @param accountName The name of the account to load.
	 * @return The loaded account, or null if no such account exists.
	 * @throws DataStoreException if an error occurs loading the account.
	 */
	public final Account loadAccount(String accountName) throws DataStoreException {
		if (!accountExists(accountName)) {
			return null;
		}
		return innerLoadAccount(accountName);
	}
	
	/**
	 * This does the actual loading of an account. It can be safe to assume in this
	 * method that an account actually exists.
	 * @param accountName The name of the account to load.
	 * @return The loaded account
	 * @throws DataStoreException if an error occurs loading the account.
	 */
	protected abstract Account innerLoadAccount(String accountName) throws DataStoreException;
	
	/**
	 * This saves an account to the data store. Note that this does not
	 * differentiate between creating an account and saving an account.
	 * @param account The account to save.
	 * @throws DataStoreException if an error occurs saving the account.
	 */
	public abstract void saveAccount(Account account) throws DataStoreException;
	
}
