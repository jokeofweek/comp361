package comp361.server.data.store;

import java.io.File;

import comp361.shared.Constants;

/**
 * This data store stores accounts in Java properties files.
 */
public class PropertyAccountDataStore implements AccountDataStore {

	private static final String ACCOUNTS_PATH = Constants.SERVER_DATA_PATH + "accounts/";
	
	@Override
	public boolean accountExists(String accountName) {
		return new File(getAccountPath(accountName)).exists();
	}
	
	/**
	 * Builds the path to the file for a given account.
	 * @param accountName The name of the account.
	 * @return The path to the account file.
	 */
	private String getAccountPath(String accountName) {
		return ACCOUNTS_PATH + accountName + ".properties";
	}
	
	
}
