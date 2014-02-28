package comp361.server.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;

/**
 * This class handles all the {@link GameDescriptor} information on the server.
 * It provides facilities for creating game descriptors as well as for getting
 * all descriptors.
 */
public class GameDescriptorManager {

	public Map<Integer, GameDescriptor> gameDescriptors;
	public int currentId;
	
	public GameDescriptorManager(){
		this.gameDescriptors = new HashMap<>();
		this.currentId = 1;
	}
	
	/**
	 * Create a {@link GameDescriptor} based off a {@link NewGameDescriptorPacket}.
	 * @param packet The packet.
	 * @return The filled in descriptor.
	 */
	public GameDescriptor createDescriptor(NewGameDescriptorPacket packet) {
		GameDescriptor descriptor = new GameDescriptor(currentId++, packet.name,
				packet.password, packet.maxPlayers);
		gameDescriptors.put(descriptor.getId(), descriptor);
		return descriptor;
	}
	
	
	/**
	 * Build a packet containing all the {@link GameDescriptor}.
	 * @return the packet.
	 */
	public GameDescriptorListPacket getGameDescriptorListPacket() {
		GameDescriptorListPacket packet = new GameDescriptorListPacket();
		packet.descriptors = new ArrayList<>(gameDescriptors.values());
		return packet;
	}
	
}