package comp361.client.network.handlers;

import comp361.client.GameClient;
import comp361.shared.packets.server.UpdatePlayerStatisticsPacket;

public class UpdatePlayerStatisticsPacketHandler implements ClientPacketHandler<UpdatePlayerStatisticsPacket> {
	public void handle(GameClient gameClient, UpdatePlayerStatisticsPacket packet) {
		gameClient.getPlayerManager().updateStatistics(packet.name, packet.statistics);
	}
}
