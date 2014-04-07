package comp361.server;

import com.esotericsoftware.minlog.Log;
import comp361.server.data.store.PropertyAccountDataStore;
import comp361.server.logger.StandardLogger;
import comp361.shared.Constants;

public class ServerApplication {

	public static void main(String[] args) {
		Log.set(Log.LEVEL_DEBUG);
		new GameServer(new StandardLogger(), Constants.PORT, new PropertyAccountDataStore());
	}

}
