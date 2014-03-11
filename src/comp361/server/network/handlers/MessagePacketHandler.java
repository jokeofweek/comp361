package comp361.server.network.handlers;

import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.shared.packets.shared.MessagePacket;

public class MessagePacketHandler implements ServerPacketHandler<MessagePacket> {

	@Override
	public void handle(Session session, GameServer gameServer,
			MessagePacket object) {
		// Send to everyone except for the sender.
		gameServer.getServer().sendToAllExceptTCP(session.getID(), object);
		gameServer.getLogger().debug("Player " + object.senderName + " sent " + (object.isMetaMessage ? "meta " : "") + "message \"" + object.message + "\"");
	}

}
