package comp361.shared.packets.server;

public class GameDescriptorPlayerUpdatePacket {

	/**
	 * The name of the player
	 */
	public String name;
	/**
	 * The ID of the game descriptor
	 */
	public int id;
	/**
	 * True if the player joined, or else false meaning they left
	 */
	public boolean joined;
	
}
