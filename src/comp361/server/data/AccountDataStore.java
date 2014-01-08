package comp361.server.data;

public interface AccountDataStore {

	/**
	 * This tests whether an account exists for a given name.
	 * 
	 * @param accountName The name of the account
	 * @return	true if there is an account registered with that name.
	 */
	public boolean accountExists(String accountName);
	
}
