package comp361.server.network.handlers;

import com.esotericsoftware.minlog.Log;

import comp361.server.GameServer;
import comp361.server.data.Account;
import comp361.server.data.AccountManager;
import comp361.server.data.store.AccountDataStore;
import comp361.server.data.store.DataStoreException;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.server.LoginResult;
import comp361.shared.packets.server.RegisterResult;

public class LoginPacketHandler implements ServerPacketHandler<LoginPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			LoginPacket object) {
		AccountDataStore store = gameServer.getAccountDataStore();
		AccountManager manager = gameServer.getAccountManager();
		
		// Check if the account exists
		if (!store.accountExists(object.accountName)) {
			session.sendTCP(LoginResult.NO_SUCH_ACCOUNT);
		// Check if the account is in use
		} else if (manager.isAccountConnected(object.accountName)) {
			session.sendTCP(LoginResult.ACCOUNT_IN_USE);
		} else {
			// Try to load the account
			Account account = null;
			try {
				account = store.loadAccount(object.accountName);
			} catch (DataStoreException e) {
				// If any loading error occurs, exit.
				session.sendTCP(LoginResult.LOAD_ERROR);
				return;
			}
			
			// Check credentials
			if (!account.getPassword().equals(object.password)) {
				session.sendTCP(LoginResult.INVALID_CREDENTIALS);
				return;
			}
			
			// We had valid credentials! The user is now logged in.
			session.setAccount(account);
			session.setSessionType(SessionType.LOBBY);
			session.sendTCP(LoginResult.SUCCESS);
		}
		
	}
}
