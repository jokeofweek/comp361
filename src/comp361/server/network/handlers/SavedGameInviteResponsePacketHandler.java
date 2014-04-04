package comp361.server.network.handlers;

import com.esotericsoftware.kryonet.Connection;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;
import comp361.shared.packets.server.GameDescriptorStartPacket;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.server.SavedGameContainer;
import comp361.shared.packets.shared.SavedGameInviteResponsePacket;

public class SavedGameInviteResponsePacketHandler implements
		ServerPacketHandler<SavedGameInviteResponsePacket> {
	@Override
	public void handle(Session session, GameServer gameServer,
			SavedGameInviteResponsePacket packet) {
		// Find the other playe'rs connection, as we need it either way
		Session otherSession = null;
		GameDescriptor desc = packet.container.descriptor;
		String otherPlayer = desc.getPlayers()[0].equals(session.getAccount().getName()) ?
				desc.getPlayers()[1] : desc.getPlayers()[0];
		for (Connection c : gameServer.getServer().getConnections()) {
			Session s = (Session)c;
			if (s.getSessionType() == SessionType.LOBBY) {
				// Test if the name is the same
				if (s.getAccount().getName().equals(otherPlayer)) {
					otherSession = s;
					break;
				}
			}
		}
		
		// If we rejected, simply forward to other player
		if (!packet.accepted) {
			otherSession.sendTCP(packet);
		} else {
			// Handle actually loading. We need to reload the container
			// to get the game information since we discarded it.
			SavedGameContainer container = gameServer.getSaveGameManager().loadGame(packet.container.fileName);
			
			// Update the descriptor with a new ID.
			gameServer.getGameDescriptorManager().loadDescriptor(container.descriptor);
			
			// Send a game descriptor created packet to everyone
			GameDescriptorCreatedPacket createdPacket = new GameDescriptorCreatedPacket();
			createdPacket.descriptor = container.descriptor;
			gameServer.getServer().sendToAllTCP(createdPacket);
			
			// Update both sessions to have the descriptor id
			session.setGameDescriptorId(container.descriptor.getId());
			session.setSessionType(SessionType.GAME);
			otherSession.setGameDescriptorId(container.descriptor.getId());
			otherSession.setSessionType(SessionType.GAME);
			
			// Send a start packet to everyone to let them know the game started
			GameDescriptorStartPacket gdStartPacket = new GameDescriptorStartPacket();
			gdStartPacket.id = session.getGameDescriptorId();
			gameServer.getServer().sendToAllTCP(gdStartPacket);
			gameServer.getLogger().debug("Game " + session.getGameDescriptorId() + " loaded");
			
			// Send the game to the two players
			GameStartPacket startPacket = new GameStartPacket();
			startPacket.game = container.game;
			session.sendTCP(startPacket);
			otherSession.sendTCP(startPacket);
		}

	}
}
