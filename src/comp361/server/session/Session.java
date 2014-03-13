package comp361.server.session;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.data.Account;
import comp361.shared.data.PlayerUpdateStatus;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.PlayerUpdatePacket;

public class Session extends Connection {

	private GameServer gameServer;
	private Account account;
	private SessionType sessionType;
	private int gameDescriptorId;

	public Session(GameServer gameServer) {
		this.gameServer = gameServer;
		// Set the session type to anonymous at connection time.
		this.sessionType = SessionType.ANONYMOUS;
	}

	/**
	 * Update the session type.
	 * 
	 * @param sessionType
	 *            the new type of the session.
	 */
	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * @return the current session type.
	 */
	public SessionType getSessionType() {
		return sessionType;
	}

	/**
	 * @return the Account associated with the Session, or null if there is none
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Updates the account associated with this session
	 * 
	 * @param account
	 */
	public void setAccount(Account account) {
		// If there was an old account, notify the account manager.
		if (this.account != null) {
			this.gameServer.getAccountManager().removeAccount(
					this.account.getName());
		}
		this.account = account;
		// If there is a new account, notify the account manager.
		if (this.account != null) {
			this.gameServer.getAccountManager().addAccount(this.account);
		}
	}

	public int getGameDescriptorId() {
		return gameDescriptorId;
	}

	public void setGameDescriptorId(int gameDescriptorId) {
		this.gameDescriptorId = gameDescriptorId;
	}

	/**
	 * Effectively disconnects the session, changing the session type and
	 * performing any required processing.
	 */
	public void disconnect() {

		// Notify all other players
		if (this.getAccount() != null) {
			// Handle player disconnecting during game setup
			if (sessionType == SessionType.GAME_SETUP) {
				GameDescriptorPlayerUpdatePacket gdPacket = new GameDescriptorPlayerUpdatePacket();
				gdPacket.id = gameDescriptorId;
				gdPacket.name = getAccount().getName();
				gdPacket.joined = false;
				gameServer.getGameDescriptorManager().removePlayer(gdPacket.id,
						gdPacket.name);
				gameServer.getServer().sendToAllExceptTCP(this.getID(),
						gdPacket);
			} else if (sessionType == SessionType.GAME) {
				gameServer.getGameDescriptorManager().endGame(
						getGameDescriptorId(), gameServer, false,
						this.getAccount().getName(),
						this.getAccount().getName() + " disconnected", true, true);
			}

			PlayerUpdatePacket updatePacket = new PlayerUpdatePacket();
			updatePacket.player = account.getPlayer();
			updatePacket.status = PlayerUpdateStatus.LOGGED_OUT;
			gameServer.getServer().sendToAllExceptTCP(this.getID(),
					updatePacket);
		}

		// Update the session type.
		this.setSessionType(SessionType.DISCONNECTED);
		// Update the account to be null (removing it from the manager)
		this.setAccount(null);

	}
}
