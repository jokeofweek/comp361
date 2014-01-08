package comp361.server;

import comp361.server.console.ConsoleImpl;
import comp361.server.data.store.PropertyAccountDataStore;
import comp361.shared.Constants;

public class ServerApplication {

	public static void main(String[] args) {
		new GameServer(new ConsoleImpl(), Constants.PORT, new PropertyAccountDataStore());
	}

}
