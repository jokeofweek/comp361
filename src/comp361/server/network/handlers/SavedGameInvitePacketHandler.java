package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.shared.SavedGameInvitePacket;

public class SavedGameInvitePacketHandler implements
		ServerPacketHandler<SavedGameInvitePacket> {
	public void handle(Session session, GameServer gameServer,
			SavedGameInvitePacket packet) {
		// Get the player that should be receiving the invite.
		GameDescriptor desc = packet.container.descriptor;
		String otherPlayer = desc.getPlayers()[0].equals(session.getAccount().getName()) ?
				desc.getPlayers()[1] : desc.getPlayers()[0];
				
		// Find the player's connection
		for (Connection c : gameServer.getServer().getConnections()) {
			Session s = (Session)c;
			if (s.getSessionType() == SessionType.LOBBY) {
				// Test if the name is the same
				if (s.getAccount().getName().equals(otherPlayer)) {
					s.sendTCP(packet);
					return;
				}
			}
		}

	}
}
