package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.data.Account;
import comp361.server.data.store.AccountDataStore;
import comp361.server.data.store.DataStoreException;
import comp361.server.session.Session;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.RegisterResult;

public class RegisterPacketHandler implements ServerPacketHandler<RegisterPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			RegisterPacket object) {
		AccountDataStore store = gameServer.getAccountDataStore();

		if (store.accountExists(object.accountName)) {
			session.sendTCP(RegisterResult.ACCOUNT_ALREADY_EXISTS);
		} else {
			// For now we simply create the account here.
			// TODO: Put this in an actual class.
			Account account = new Account();
			account.setName(object.accountName);
			account.setPassword(object.password);
			
			// Try to save the account, sending the appropriate result.
			try {
				store.saveAccount(account);
				session.sendTCP(RegisterResult.SUCCESS);
			} catch (DataStoreException e) {
				session.sendTCP(RegisterResult.SAVE_ERROR);
			}
		}
		
	}
}
