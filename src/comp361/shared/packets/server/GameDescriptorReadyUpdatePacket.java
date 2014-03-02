package comp361.shared.packets.server;

import java.util.HashSet;

public class GameDescriptorReadyUpdatePacket {

	public int id;
	public HashSet<String> readyPlayers;
	
}
