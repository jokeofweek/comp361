package comp361.server.data.store;

/**
 * This wraps around an exception and allows us to have less implementation-specific
 * exceptions raised by a data store.
 */
public class DataStoreException extends Exception {

	public DataStoreException(String message) {
		super(message);
	}
	
	public DataStoreException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
