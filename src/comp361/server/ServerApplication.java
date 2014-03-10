package comp361.server;

import comp361.server.data.store.PropertyAccountDataStore;
import comp361.server.logger.StandardLogger;
import comp361.shared.Constants;

public class ServerApplication {

	public static void main(String[] args) {
		new GameServer(new StandardLogger(), Constants.PORT, new PropertyAccountDataStore());
	}

}
