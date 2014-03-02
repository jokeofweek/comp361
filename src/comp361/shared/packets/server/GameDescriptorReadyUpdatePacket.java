package comp361.shared.packets.server;

import java.util.HashSet;

public class GameDescriptorReadyUpdatePacket {

	/**
	 * The name of the player
	 */
	public String name;
	/**
	 * The ID of the game descriptor
	 */
	public int id;
	/**
	 * True if the player is ready, or else false meaning they cancelled ready
	 */
	public boolean ready;
}
