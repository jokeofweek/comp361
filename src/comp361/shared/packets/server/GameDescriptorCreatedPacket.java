package comp361.shared.packets.server;

import comp361.shared.data.GameDescriptor;

public class GameDescriptorCreatedPacket {
	/**
	 * The descriptor itself.
	 */
	public GameDescriptor descriptor;
	/**
	 * If this value is true, then the client should auto join the game.
	 */
	public boolean isCreator;
}
