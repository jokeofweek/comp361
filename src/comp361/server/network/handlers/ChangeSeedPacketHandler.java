package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.packets.shared.ChangeSeedPacket;

public class ChangeSeedPacketHandler implements
		ServerPacketHandler<ChangeSeedPacket> {

	@Override
	public void handle(Session session, GameServer gameServer,
			ChangeSeedPacket packet) {
		// Update the game descriptor
		gameServer.getGameDescriptorManager().updateSeed(packet.id, packet.seed);
		
		gameServer.getLogger().debug("Updating seed for game " + packet.id + " to " + packet.seed);
		
		// Forward the message to all players
		gameServer.getServer().sendToAllTCP(packet);
	}
}
