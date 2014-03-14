package comp361.server.data.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import comp361.server.data.Account;
import comp361.shared.Constants;
import comp361.shared.data.Statistics;

/**
 * This data store stores accounts in Java properties files.
 */
public class PropertyAccountDataStore extends AccountDataStore {

	private static final String ACCOUNTS_PATH = Constants.SERVER_DATA_PATH + "accounts/";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_WINS = "wins";
	private static final String FIELD_LOSSES = "losses";
	private static final String FIELD_DRAWS = "draws";
	
	/**
	 * Builds the path to the file for a given account.
	 * @param accountName The name of the account.
	 * @return The path to the account file.
	 */
	private String getAccountPath(String accountName) {
		return ACCOUNTS_PATH + accountName + ".properties";
	}
	
	@Override
	public boolean accountExists(String accountName) {
		return new File(getAccountPath(accountName)).exists();
	}
	
	@Override
	protected Account innerLoadAccount(String accountName) throws DataStoreException {
		
		Properties properties = new Properties();
		Reader fileReader = null;
		
		try {
			// Try to load the properties file.
			fileReader = new FileReader(getAccountPath(accountName)); 
			properties.load(fileReader);
		} catch (IOException e) {
			// Wrap the exception in a data store exception.
			throw new DataStoreException("An error occured loading the account.", e);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {}
			}
		}
		
		// Create the account and load in the properties
		Account account = new Account(accountName);
		account.setPassword(properties.getProperty(FIELD_PASSWORD));
		// Update the statistics
		Statistics stats = account.getPlayer().getStatistics();
		stats.setWins(Integer.parseInt(properties.getProperty(FIELD_WINS, "0")));
		stats.setLosses(Integer.parseInt(properties.getProperty(FIELD_LOSSES, "0")));
		stats.setDraws(Integer.parseInt(properties.getProperty(FIELD_DRAWS, "0")));
		
		return account;
	}

	@Override
	public void saveAccount(Account account) throws DataStoreException {
		Properties properties = new Properties();
		
		// Convert the account data to properties
		properties.put(FIELD_PASSWORD, account.getPassword());
		Statistics stats = account.getPlayer().getStatistics();
		properties.put(FIELD_WINS, "" + stats.getWins());
		properties.put(FIELD_LOSSES, "" + stats.getLosses());
		properties.put(FIELD_DRAWS, "" + stats.getDraws());
		
		// Save the properties
		OutputStream out = null;
		try {
			out = new FileOutputStream(getAccountPath(account.getName()));
			properties.store(out, null);
		}
		catch (IOException e) {
			// Wrap the exception
			throw new DataStoreException("An error occured saving the account.", e);
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
