package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.data.Account;
import comp361.server.data.store.AccountDataStore;
import comp361.server.data.store.DataStoreException;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.server.RegisterError;

public class RegisterPacketHandler implements ServerPacketHandler<RegisterPacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			RegisterPacket object) {
		AccountDataStore store = gameServer.getAccountDataStore();

		if (store.accountExists(object.accountName)) {
			gameServer.getLogger().warn("Account " + object.accountName + " already exists");
			session.sendTCP(RegisterError.ACCOUNT_ALREADY_EXISTS);
		} else {
			// Create the account
			Account account = new Account();
			account.setName(object.accountName);
			account.setPassword(object.password);
			
			// Try to save the account, sending the appropriate result or
			// returning if there was an error
			try {
				store.saveAccount(account);
				gameServer.getLogger().debug("Account " + object.accountName + " created");
			} catch (DataStoreException e) {
				session.sendTCP(RegisterError.SAVE_ERROR);
				gameServer.getLogger().error("Could not create account " + object.accountName);
				return;
			}
			
			// Once the account was saved, we can update the player and change
			// their session type.
			session.setAccount(account);
			session.setSessionType(SessionType.LOBBY);

			// If successful, send the initial data packets
			session.sendTCP(gameServer.getAccountManager().getPlayerListPacket());
			session.sendTCP(gameServer.getGameDescriptorManager().getGameDescriptorListPacket());
		}
		
	}
}
