package comp361.server.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

	private Map<Integer, GameDescriptor> gameDescriptors;
	private int currentId;

	public GameDescriptorManager() {
		this.gameDescriptors = new HashMap<>();
		this.currentId = 1;
	}

	/**
	 * Create a {@link GameDescriptor} based off a
	 * {@link NewGameDescriptorPacket}.
	 * 
	 * @param packet
	 *            The packet.
	 * @return The filled in descriptor.
	 */
	public GameDescriptor createDescriptor(NewGameDescriptorPacket packet) {
		GameDescriptor descriptor = new GameDescriptor(currentId++,
				packet.name, packet.password, packet.maxPlayers);
		descriptor.setSeed(System.currentTimeMillis());
		gameDescriptors.put(descriptor.getId(), descriptor);
		return descriptor;
	}

	/**
	 * Checks whether there is space for an extra player in a game.
	 * 
	 * @param id
	 *            The ID of the descriptor.
	 * @return True if there are less players in the game.
	 */
	public boolean gameHasSpace(int id) {
		return gameDescriptors.get(id).getPlayers().size() < gameDescriptors
				.get(id).getMaxPlayers();
	}

	/**
	 * Checks whether the game has started or not.
	 * 
	 * @param id
	 *            The id of the descriptor.
	 * @return True if the game has started.
	 */
	public boolean gameHasStarted(int id) {
		return gameDescriptors.get(id).isStarted();
	}

	/**
	 * This adds a player to a given game descriptor
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name of the player
	 * @return True if the player was added, or false if there was no space.
	 */
	public boolean addPlayer(int id, String name) {
		if (!gameHasSpace(id)) {
			return false;
		}

		gameDescriptors.get(id).addPlayer(name);

		return true;
	}

	/**
	 * This removes a player from a given game descriptor
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name of the player
	 */
	public void removePlayer(int id, String name) {
		gameDescriptors.get(id).removePlayer(name);
		// Remove the descriptor if no players left
		if (!gameDescriptors.get(id).hasPlayers()) {
			gameDescriptors.remove(id);
		}
	}

	/**
	 * Update a game's seed.
	 * 
	 * @param id
	 *            The id of the game.
	 * @param seed
	 *            The seed of the game.
	 */
	public void updateSeed(int id, long seed) {
		gameDescriptors.get(id).setSeed(seed);
		gameDescriptors.get(id).clearReadyPlayers();
	}

	/**
	 * Build a packet containing all the {@link GameDescriptor}.
	 * 
	 * @return the packet.
	 */
	public GameDescriptorListPacket getGameDescriptorListPacket() {
		GameDescriptorListPacket packet = new GameDescriptorListPacket();
		packet.descriptors = new ArrayList<>(gameDescriptors.values());
		return packet;
	}

}
