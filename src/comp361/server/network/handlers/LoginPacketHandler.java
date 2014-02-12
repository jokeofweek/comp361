package comp361.server.network.handlers;

import java.util.Set;

import com.esotericsoftware.minlog.Log;

import comp361.server.GameServer;
import comp361.server.data.Account;
import comp361.server.data.AccountManager;
import comp361.server.data.store.AccountDataStore;
import comp361.server.data.store.DataStoreException;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.PlayerUpdateStatus;
import comp361.shared.data.Statistics;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.PlayerListPacket;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.server.RegisterError;

public class LoginPacketHandler implements ServerPacketHandler<LoginPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			LoginPacket object) {
		AccountDataStore store = gameServer.getAccountDataStore();
		AccountManager manager = gameServer.getAccountManager();
		
		// Check if the account exists
		if (!store.accountExists(object.accountName)) {
			session.sendTCP(LoginError.NO_SUCH_ACCOUNT);
		// Check if the account is in use
		} else if (manager.isAccountConnected(object.accountName)) {
			session.sendTCP(LoginError.ACCOUNT_IN_USE);
		} else {
			// Try to load the account
			Account account = null;
			try {
				account = store.loadAccount(object.accountName);
			} catch (DataStoreException e) {
				// If any loading error occurs, exit.
				session.sendTCP(LoginError.LOAD_ERROR);
				return;
			}
			
			// Check credentials
			if (!account.getPassword().equals(object.password)) {
				session.sendTCP(LoginError.INVALID_CREDENTIALS);
				return;
			}
			
			// We had valid credentials! The user is now logged in.
			session.setAccount(account);
			session.setSessionType(SessionType.LOBBY);
			
			// If successful, send the player list packet
			session.sendTCP(manager.getPlayerListPacket());
			
			// Send the login to all other players
			PlayerUpdatePacket updatePacket = new PlayerUpdatePacket();
			updatePacket.player = account.getPlayer();
			updatePacket.status = PlayerUpdateStatus.LOGGED_IN;
			gameServer.getServer().sendToAllTCP(updatePacket);
		}
	}
}
